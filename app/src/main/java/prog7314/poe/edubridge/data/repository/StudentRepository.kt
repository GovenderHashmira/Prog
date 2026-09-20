package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.local.dao.StudentDao
import prog7314.poe.edubridge.data.mapper.toDomain
import prog7314.poe.edubridge.data.mapper.toEntity
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.domain.model.Student
import prog7314.poe.edubridge.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val dao: StudentDao
) {
    private companion object { const val TAG = "EduBridge-Repo-Student" }

    fun observeStudents(): Flow<List<Student>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeStudent(studentId: String): Flow<Student?> =
        dao.observeById(studentId).map { it?.toDomain() }

    suspend fun refreshStudents(): Resource<List<Student>> {
        Log.d(TAG, "Refreshing students from API")
        return try {
            val dtos = api.getLinkedStudents()
            if (dtos.isEmpty()) {
                Log.w(TAG, "API returned empty student list")
                return Resource.Empty
            }
            dao.upsertAll(dtos.map { it.toEntity() })
            Log.d(TAG, "Cached ${dtos.size} students")
            Resource.Success(dtos.map { it.toDomain() })
        } catch (e: IOException) {
            Log.e(TAG, "Network failure refreshing students", e)
            val cached = dao.observeAll().let { flow ->
                // Just report cache exists; ViewModel already observes Flow
                Resource.Error("Offline — showing cached students", e)
            }
            cached
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error refreshing students", e)
            Resource.Error("Failed to load students", e)
        }
    }
}