package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** POST /api/auth/sso — request body. */
data class AuthRequest(
    @SerializedName("provider")     val provider: String,      // "google" | "azure" | "apple"
    @SerializedName("identityToken") val identityToken: String,
    @SerializedName("deviceId")     val deviceId: String
)

/** POST /api/auth/sso — response body. */
data class AuthResponse(
    @SerializedName("accessToken")  val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("expiresIn")    val expiresIn: Int,        // seconds
    @SerializedName("user")         val user: UserProfileDto
)

/** POST /api/auth/refresh — request body. */
data class RefreshRequest(
    @SerializedName("refreshToken") val refreshToken: String
)