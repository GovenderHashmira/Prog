package prog7314.poe.edubridge.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import prog7314.poe.edubridge.data.UserPreferences
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injects the Bearer token into every outgoing request.
 * Skips auth for the SSO endpoint (login does not require a token yet).
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val prefs: UserPreferences
) : Interceptor {

    private companion object {
        const val TAG = "EduBridge-Net"
        const val AUTH_HEADER = "Authorization"
        const val BEARER = "Bearer "
        const val SKIP_PATH = "api/auth/sso"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val path = original.url.encodedPath

        // Login endpoint does not carry a token yet.
        if (path.contains(SKIP_PATH)) {
            Log.d(TAG, "Skipping auth for $path")
            return chain.proceed(original)
        }

        val token = runBlocking { prefs.getAccessToken() }

        val request = original.newBuilder()
            .apply {
                if (!token.isNullOrBlank()) {
                    addHeader(AUTH_HEADER, BEARER + token)
                } else {
                    Log.w(TAG, "No token available for $path")
                }
                addHeader("Accept", "application/json")
            }
            .build()

        Log.d(TAG, "→ ${request.method} $path")
        return try {
            val response = chain.proceed(request)
            Log.d(TAG, "← ${response.code} $path")
            response
        } catch (e: IOException) {
            Log.e(TAG, "Network failure for $path", e)
            throw e
        }
    }
}