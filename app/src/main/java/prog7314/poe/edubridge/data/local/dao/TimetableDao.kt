package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.ClassPeriodEntity
import prog7314.poe.edubridge.data.local.entity.TimetableEntity

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable WHERE studentId = :studentId LIMIT 1")
    fun observeTimetable(studentId: String): Flow<TimetableEntity?>

    @Query("""
        SELECT * FROM class_periods
        WHERE timetableId = :timetableId
        ORDER BY dayOfWeek ASC, startTime ASC
    """)
    fun observePeriods(timetableId: String): Flow<List<ClassPeriodEntity>>

    @Query("""
        SELECT * FROM class_periods
        WHERE timetableId = :timetableId AND dayOfWeek = :dayOfWeek
        ORDER BY startTime ASC
    """)
    fun observePeriodsForDay(
        timetableId: String,
        dayOfWeek: Int
    ): Flow<List<ClassPeriodEntity>>

    @Query("SELECT * FROM class_periods WHERE periodId = :periodId LIMIT 1")
    suspend fun getPeriodById(periodId: String): ClassPeriodEntity?

    @Upsert
    suspend fun upsertTimetable(timetable: TimetableEntity)

    @Upsert
    suspend fun upsertPeriods(periods: List<ClassPeriodEntity>)

    @Query("DELETE FROM class_periods WHERE timetableId = :timetableId")
    suspend fun clearPeriods(timetableId: String)

    @Query("DELETE FROM timetable WHERE studentId = :studentId")
    suspend fun clearForStudent(studentId: String)

    /** Atomically replace the timetable and its periods. */
    @Transaction
    suspend fun replaceTimetable(
        timetable: TimetableEntity,
        periods: List<ClassPeriodEntity>
    ) {
        clearPeriods(timetable.timetableId)
        upsertTimetable(timetable)
        upsertPeriods(periods)
    }

    @Transaction
    suspend fun clearForStudentFully(studentId: String) {
        val current = getTimetableForStudent(studentId)
        current?.let { clearPeriods(it.timetableId) }
        clearForStudent(studentId)
    }

    @Query("SELECT * FROM timetable WHERE studentId = :studentId LIMIT 1")
    suspend fun getTimetableForStudent(studentId: String): TimetableEntity?
}