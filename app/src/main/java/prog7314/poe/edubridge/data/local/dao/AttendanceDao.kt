package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.AttendanceEntity

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun observeByStudent(studentId: String): Flow<List<AttendanceEntity>>

    @Query("""
        SELECT * FROM attendance
        WHERE studentId = :studentId AND date BETWEEN :from AND :to
        ORDER BY date DESC
    """)
    fun observeByRange(
        studentId: String,
        from: String,
        to: String
    ): Flow<List<AttendanceEntity>>

    @Query("""
        SELECT * FROM attendance
        WHERE studentId = :studentId AND status = :status
        ORDER BY date DESC
    """)
    fun observeByStatus(studentId: String, status: String): Flow<List<AttendanceEntity>>

    @Query("""
        SELECT COUNT(*) FROM attendance
        WHERE studentId = :studentId AND status = 'PRESENT'
    """)
    fun observePresentCount(studentId: String): Flow<Int>

    @Query("SELECT * FROM attendance WHERE attendanceId = :attendanceId LIMIT 1")
    suspend fun getById(attendanceId: String): AttendanceEntity?

    @Upsert
    suspend fun upsertAll(records: List<AttendanceEntity>)

    @Upsert
    suspend fun upsert(record: AttendanceEntity)

    @Query("DELETE FROM attendance WHERE studentId = :studentId")
    suspend fun clearForStudent(studentId: String)

    @Query("DELETE FROM attendance")
    suspend fun clear()
}