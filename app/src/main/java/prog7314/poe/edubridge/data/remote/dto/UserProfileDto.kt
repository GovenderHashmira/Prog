package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/users/me — response body. */
data class UserProfileDto(
    @SerializedName("userId")   val userId: String,
    @SerializedName("name")     val name: String,
    @SerializedName("email")    val email: String,
    @SerializedName("role")     val role: String,        // "PARENT" | "STUDENT" | "TEACHER" | "ADMIN"
    @SerializedName("language") val language: String = "en",
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)