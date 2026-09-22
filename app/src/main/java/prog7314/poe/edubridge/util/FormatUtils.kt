package prog7314.poe.edubridge.util

import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object FormatUtils {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

    fun relativeTime(instant: Instant): String {
        val now = Instant.now()
        val minutes = Duration.between(instant, now).toMinutes()
        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            minutes < 1440 -> "${minutes / 60}h ago"
            minutes < 2880 -> "Yesterday"
            else -> "${minutes / 1440}d ago"
        }
    }

    fun formatDate(isoDate: String): String = try {
        LocalDate.parse(isoDate).format(dateFormatter)
    } catch (e: Exception) {
        isoDate
    }

    fun dayLabel(dayOfWeek: Int): String {
        val day = ((dayOfWeek - 1).coerceIn(0, 6)) + 1
        return DayOfWeek.of(day)
            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}