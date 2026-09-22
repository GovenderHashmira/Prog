package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.StudentEntity

@Dao
interface StudentDao {

    @Query("SELECT * FROM students WHERE active = 1 ORDER BY firstName ASC")
    fun observeAll(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE userId = :userId AND active = 1 ORDER BY firstName ASC")
    fun observeByUser(userId: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
    fun observeById(studentId: String): Flow<StudentEntity?>

    @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
    suspend fun getById(studentId: String): StudentEntity?

    @Query("SELECT * FROM students WHERE userId = :userId")
    suspend fun getForUser(userId: String): List<StudentEntity>

    @Upsert
    suspend fun upsertAll(students: List<StudentEntity>)

    @Upsert
    suspend fun upsert(student: StudentEntity)

    @Query("DELETE FROM students WHERE studentId = :studentId")
    suspend fun deleteById(studentId: String)

    @Query("DELETE FROM students")
    suspend fun clear()
}