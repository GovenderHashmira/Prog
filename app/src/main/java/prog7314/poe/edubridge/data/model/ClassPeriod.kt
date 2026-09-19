package prog7314.poe.edubridge.data.model


import java.time.LocalTime

/**
 * One scheduled class period within a timetable.
 */
data class ClassPeriod(
    val id: String,
    val subjectName: String,
    val teacherName: String,
    val location: String? = null,
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    val timeRange: String
        get() = "${startTime.format(SHORT)} – ${endTime.format(SHORT)}"

    val durationMinutes: Long
        get() = java.time.Duration.between(startTime, endTime).toMinutes()

    companion object {
        private val SHORT = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
    }
}