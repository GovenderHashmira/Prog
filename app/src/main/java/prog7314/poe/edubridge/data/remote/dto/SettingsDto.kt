package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** PUT /api/users/me/settings — request and response body. */
data class SettingsDto(
    @SerializedName("language")            val language: String,
    @SerializedName("notificationEnabled") val notificationEnabled: Boolean,
    @SerializedName("biometricEnabled")    val biometricEnabled: Boolean,
    @SerializedName("darkModeEnabled")     val darkModeEnabled: Boolean,
    @SerializedName("weatherCity")         val weatherCity: String
)