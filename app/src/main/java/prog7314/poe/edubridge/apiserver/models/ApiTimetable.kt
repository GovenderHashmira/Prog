package prog7314.poe.edubridge.apiserver.models

data class ApiTimetable(
    val id: String,
    val studentId: String,
    val day: String,
    val startTime: String,
    val endTime: String,
    val subject: String,
    val teacher: String,
    val room: String
)