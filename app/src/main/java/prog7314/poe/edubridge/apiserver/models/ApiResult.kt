package prog7314.poe.edubridge.apiserver.models

data class ApiResult(
    val id: String,
    val studentId: String,
    val subject: String,
    val mark: Double,
    val assessmentName: String,
    val period: String,
    val teacherComment: String = "",
    val updatedAt: String = ""
)