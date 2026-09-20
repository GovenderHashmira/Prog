package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.MarkEntity

@Dao
interface MarkDao {

    @Query("SELECT * FROM marks WHERE studentId = :studentId ORDER BY subjectName ASC")
    fun observeByStudent(studentId: String): Flow<List<MarkEntity>>

    @Query("""
        SELECT * FROM marks
        WHERE studentId = :studentId AND academicPeriod = :period
        ORDER BY subjectName ASC
    """)
    fun observeByPeriod(studentId: String, period: String): Flow<List<MarkEntity>>

    @Query("SELECT * FROM marks WHERE studentId = :studentId AND subjectId = :subjectId")
    fun observeBySubject(studentId: String, subjectId: String): Flow<List<MarkEntity>>

    @Query("SELECT * FROM marks WHERE markId = :markId LIMIT 1")
    suspend fun getById(markId: String): MarkEntity?

    @Upsert
    suspend fun upsertAll(marks: List<MarkEntity>)

    @Upsert
    suspend fun upsert(mark: MarkEntity)

    @Query("DELETE FROM marks WHERE studentId = :studentId")
    suspend fun clearForStudent(studentId: String)

    @Query("DELETE FROM marks")
    suspend fun clear()
}