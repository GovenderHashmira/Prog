package prog7314.poe.edubridge.apiserver.models

data class ApiLoginResponse(
    val token: String,
    val role: String,
    val userId: String,
    val userName: String,
    val email: String
)