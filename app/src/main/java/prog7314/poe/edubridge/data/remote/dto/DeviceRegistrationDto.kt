package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** POST /api/devices/register — request body. */
data class DeviceRegistrationDto(
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("platform") val platform: String = "android"
)