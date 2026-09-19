package prog7314.poe.edubridge.data.model
/**
 * Weekly timetable container for one student within an academic context.
 */
data class Timetable(
    val id: String,
    val studentId: String,
    val academicYear: Int,
    val term: Int,
    val periods: List<ClassPeriod> = emptyList()
) {
    /** Returns periods for a given day (1 = Monday … 7 = Sunday). */
    fun periodsForDay(dayOfWeek: Int): List<ClassPeriod> =
        periods.filter { it.dayOfWeek == dayOfWeek }
            .sortedBy { it.startTime }

    /** Groups periods by day, sorted chronologically. */
    fun byDay(): Map<Int, List<ClassPeriod>> =
        periods.groupBy { it.dayOfWeek }
            .toSortedMap()
            .mapValues { (_, list) -> list.sortedBy { it.startTime } }
}