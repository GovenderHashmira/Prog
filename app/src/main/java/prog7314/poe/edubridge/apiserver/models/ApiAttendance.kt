package prog7314.poe.edubridge.apiserver.models


data class ApiAttendance(
    val id: String,
    val studentId: String,
    val date: String,
    val status: String,
    val comment: String = ""
)