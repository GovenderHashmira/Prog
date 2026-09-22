package prog7314.poe.edubridge.apiserver

object ApiConfig {

    // Port embedded Ktor server listens on.
    const val PORT = 8080

    // HOST works from the same device.
    const val HOST = "127.0.0.1"

    // Base URL that member 3's Retofit client should use.
    const val BASE_URL = "http://$HOST:$PORT/"

    // Log tag used across all API classes.
    const val TAG = "EduBridgeApi"

    // Mock token lifetime (ms) - info only.
    const val TOKEN_LIFETIME_MS =60 * 60 * 1000L
}