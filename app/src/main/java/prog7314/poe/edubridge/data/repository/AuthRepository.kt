package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.mapper.*
import prog7314.poe.edubridge.data.local.dao.*
import prog7314.poe.edubridge.data.remote.*
import prog7314.poe.edubridge.data.remote.dto.*
import prog7314.poe.edubridge.util.*
import kotlinx.coroutines.flow.*
import prog7314.poe.edubridge.data.*
import prog7314.poe.edubridge.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import prog7314.poe.edubridge.BuildConfig
import prog7314.poe.edubridge.apiserver.EduBridgeApiServer
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val userDao: UserDao,
    private val prefs: UserPreferences
) {
    private companion object { const val TAG = "EduBridge-Repo-Auth" }

    fun observeCurrentUser(): Flow<User?> =
        userDao.observeCurrent().map { it?.toDomain() }

    // Runs on IO: EncryptedSharedPreferences/Keystore and Room are too slow for the main thread.
    suspend fun login(provider: String, identityToken: String, deviceId: String): Resource<User> =
        withContext(Dispatchers.IO) { doLogin(provider, identityToken, deviceId) }

    private suspend fun doLogin(provider: String, identityToken: String, deviceId: String): Resource<User> {
        Log.d(TAG, "Login attempt via provider=$provider")

        // Debug builds talk to the embedded server; make sure it is actually up.
        if (BuildConfig.API_BASE_URL.contains("127.0.0.1") && !EduBridgeApiServer.awaitReady()) {
            val cause = EduBridgeApiServer.startupError
            Log.e(TAG, "Embedded API server not available", cause)
            return Resource.Error(
                if (cause != null) "Local server failed to start: ${cause.javaClass.simpleName}: ${cause.message}"
                else "Local server is still starting. Please try again in a moment.",
                cause
            )
        }

        return try {
            val response = api.authenticateSso(
                AuthRequest(provider = provider, identityToken = identityToken, deviceId = deviceId)
            )
            prefs.saveAccessToken(response.accessToken)
            response.refreshToken?.let { prefs.saveRefreshToken(it) }
            val entity = response.user.toDomain().toEntity()
            userDao.upsert(entity)
            Log.d(TAG, "Login succeeded userId=${entity.userId}")
            Resource.Success(entity.toDomain())
        } catch (e: HttpException) {
            Log.e(TAG, "Login rejected: HTTP ${e.code()}", e)
            Resource.Error(
                when (e.code()) {
                    401 -> "Incorrect email or password"
                    409 -> "An account with this email already exists"
                    400 -> "Please enter your email and password"
                    else -> "Server error (${e.code()}). Please try again."
                },
                e
            )
        } catch (e: IOException) {
            Log.e(TAG, "Network failure during login", e)
            // Include the exception type so the real problem is visible on screen.
            Resource.Error("Couldn't reach the server (${e.javaClass.simpleName}: ${e.message})", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during login", e)
            Resource.Error("Login failed (${e.javaClass.simpleName}: ${e.message})", e)
        }
    }

    suspend fun refresh(): Resource<Unit> = try {
        val token = prefs.getRefreshToken() ?: return Resource.Error("No refresh token")
        val response = api.refresh(RefreshRequest(token))
        prefs.saveAccessToken(response.accessToken)
        Log.d(TAG, "Token refreshed")
        Resource.Success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "Token refresh failed", e)
        Resource.Error("Session expired", e)
    }

    suspend fun logout() {
        Log.i(TAG, "Logging out")
        prefs.clearSession()
        userDao.clear()
    }
}