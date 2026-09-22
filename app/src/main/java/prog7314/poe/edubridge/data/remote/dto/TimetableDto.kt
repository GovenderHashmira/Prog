package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/students/{id}/timetable — response body. */
data class TimetableDto(
    @SerializedName("timetableId")  val timetableId: String,
    @SerializedName("studentId")    val studentId: String,
    @SerializedName("academicYear") val academicYear: Int,
    @SerializedName("term")         val term: Int,
    @SerializedName("periods")      val periods: List<ClassPeriodDto> = emptyList()
)

/** Nested element inside `TimetableDto`. */
data class ClassPeriodDto(
    @SerializedName("periodId")    val periodId: String,
    @SerializedName("timetableId") val timetableId: String,
    @SerializedName("subjectId")   val subjectId: String,
    @SerializedName("subjectName") val subjectName: String,
    @SerializedName("teacherName") val teacherName: String,
    @SerializedName("location")    val location: String? = null,
    @SerializedName("dayOfWeek")   val dayOfWeek: Int,           // 1 = Mon … 7 = Sun
    @SerializedName("startTime")   val startTime: String,        // HH:mm
    @SerializedName("endTime")     val endTime: String           // HH:mm
)