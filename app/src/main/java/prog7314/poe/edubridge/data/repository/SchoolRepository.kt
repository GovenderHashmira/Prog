package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.local.dao.*
import prog7314.poe.edubridge.data.remote.dto.*
import prog7314.poe.edubridge.data.remote.*
import prog7314.poe.edubridge.util.*
import java.io.IOException
import javax.inject.*

/**
 * Provides school metadata (name, address, lat/lng) for the Google Maps screen.
 * School data is not cached in Room — it changes rarely and is small enough
 * to fetch on demand.
 */
@Singleton
class SchoolRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val studentDao: StudentDao
) {
    private companion object { const val TAG = "EduBridge-Repo-School" }

    private var cache: SchoolDto? = null

    suspend fun getSchoolForStudent(studentId: String): Resource<SchoolDto> {
        cache?.let {
            Log.d(TAG, "Returning cached school for student=$studentId")
            return Resource.Success(it)
        }

        return try {
            val student = studentDao.getById(studentId)
                ?: return Resource.Error("Student not found: $studentId")
            Log.d(TAG, "Fetching school ${student.schoolId} for student=$studentId")
            val school = api.getSchool(student.schoolId)
            cache = school
            Log.d(TAG, "School loaded: ${school.name} @ (${school.latitude}, ${school.longitude})")
            Resource.Success(school)
        } catch (e: IOException) {
            Log.e(TAG, "Network failure fetching school", e)
            Resource.Error("Could not load school location. Check connection.", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error fetching school", e)
            Resource.Error("Could not load school location.", e)
        }
    }

    fun clearCache() {
        cache = null
    }
}