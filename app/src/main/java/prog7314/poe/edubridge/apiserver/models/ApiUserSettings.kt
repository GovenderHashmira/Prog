package prog7314.poe.edubridge.apiserver.models

data class ApiUserSettings(
    val userId: String,
    val language: String = "en",
    val notificationsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val updatedAt: String = ""
)