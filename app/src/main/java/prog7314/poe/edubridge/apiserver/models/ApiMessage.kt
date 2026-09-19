package prog7314.poe.edubridge.apiserver.models

data class ApiMessage(
    val id: String,
    val senderName: String,
    val senderRole: String,
    val subject: String,
    val body: String,
    val sentAt: String,
    val read: Boolean = false
)