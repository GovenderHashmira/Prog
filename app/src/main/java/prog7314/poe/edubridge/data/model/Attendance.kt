package prog7314.poe.edubridge.data.model

import prog7314.poe.edubridge.data.*

data class Attendance(
    val id: Int,
    val studentId: String,
    val date: String,
    val status: AttendanceStatus,
    val notes: String? = null
)