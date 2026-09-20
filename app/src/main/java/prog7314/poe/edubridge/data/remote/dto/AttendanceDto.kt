package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/students/{id}/attendance — response element. */
data class AttendanceDto(
    @SerializedName("attendanceId") val attendanceId: String,
    @SerializedName("studentId")    val studentId: String,
    @SerializedName("date")         val date: String,             // yyyy-MM-dd
    @SerializedName("status")       val status: String,           // "PRESENT" | "ABSENT" | "LATE"
    @SerializedName("notes")        val notes: String? = null,
    @SerializedName("updatedAt")    val updatedAt: String
)