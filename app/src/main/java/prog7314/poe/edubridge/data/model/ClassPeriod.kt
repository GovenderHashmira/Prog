package prog7314.poe.edubridge.data.model


import android.os.Build
import androidx.annotation.RequiresApi
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
        @RequiresApi(Build.VERSION_CODES.O)
        get() = "${startTime.format(SHORT)} – ${endTime.format(SHORT)}"

    val durationMinutes: Long
        @RequiresApi(Build.VERSION_CODES.O)
        get() = java.time.Duration.between(startTime, endTime).toMinutes()

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val SHORT = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
    }
}