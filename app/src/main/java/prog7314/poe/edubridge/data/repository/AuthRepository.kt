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

    suspend fun login(provider: String, identityToken: String, deviceId: String): Resource<User> {
        Log.d(TAG, "Login attempt via provider=$provider")
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
        } catch (e: IOException) {
            Log.e(TAG, "Network failure during login", e)
            Resource.Error("Network unavailable. Please try again.", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during login", e)
            Resource.Error("Login failed: ${e.message}", e)
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