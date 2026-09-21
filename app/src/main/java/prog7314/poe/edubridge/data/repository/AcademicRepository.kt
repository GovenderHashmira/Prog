package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.local.dao.*
import prog7314.poe.edubridge.util.Resource.*
import prog7314.poe.edubridge.data.mapper.*
import prog7314.poe.edubridge.data.remote.*
import prog7314.poe.edubridge.data.model.*
import prog7314.poe.edubridge.util.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.*

@Singleton
class AcademicRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val markDao: MarkDao,
    private val attendanceDao: AttendanceDao,
    private val timetableDao: TimetableDao
) {
    private companion object { const val TAG = "EduBridge-Repo-Academic" }

    // ── Marks ────────────────────────────────────────────
    fun observeMarks(studentId: String): Flow<List<Mark>> =
        markDao.observeByStudent(studentId).map { list -> list.map { it.toDomain() } }

    suspend fun refreshMarks(studentId: String): Resource<List<Mark>> = try {
        Log.d(TAG, "Fetching marks for student=$studentId")
        val dtos = api.getResults(studentId)
        markDao.clearForStudent(studentId)
        markDao.upsertAll(dtos.map { it.toEntity() })
        Log.d(TAG, "Cached ${dtos.size} marks")
        Success(dtos.map { it.toDomain() })
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching marks", e)
        Error("Offline — showing cached marks", e)
    }

    // ── Attendance ───────────────────────────────────────
    fun observeAttendance(studentId: String): Flow<List<Attendance>> =
        attendanceDao.observeByStudent(studentId).map { list -> list.map { it.toDomain() } }

    fun observeAttendanceRange(studentId: String, from: String, to: String): Flow<List<Attendance>> =
        attendanceDao.observeByRange(studentId, from, to).map { list -> list.map { it.toDomain() } }

    suspend fun refreshAttendance(studentId: String): Resource<List<Attendance>> = try {
        Log.d(TAG, "Fetching attendance for student=$studentId")
        val dtos = api.getAttendance(studentId)
        attendanceDao.clearForStudent(studentId)
        attendanceDao.upsertAll(dtos.map { it.toEntity() })
        Log.d(TAG, "Cached ${dtos.size} attendance records")
        Success(dtos.map { it.toDomain() })
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching attendance", e)
        Resource.Error("Offline — showing cached attendance", e)
    }

    // ── Timetable ────────────────────────────────────────
    fun observeTimetable(studentId: String): Flow<Timetable?> =
        timetableDao.observeTimetable(studentId).flatMapLatest { entity ->
            if (entity == null) {
                flowOf(null)
            } else {
                timetableDao.observePeriods(entity.timetableId).map { periodEntities ->
                    entity.toDomain(periodEntities.map { it.toDomain() })
                }
            }
        }

    fun observePeriods(timetableId: String): Flow<List<ClassPeriod>> =
        timetableDao.observePeriods(timetableId).map { list -> list.map { it.toDomain() } }

    suspend fun refreshTimetable(studentId: String): Resource<Timetable> = try {
        Log.d(TAG, "Fetching timetable for student=$studentId")
        val dto = api.getTimetable(studentId)
        val timetable = dto.toEntity()
        val periods = dto.periods.map { it.toEntity() }
        timetableDao.replaceTimetable(timetable, periods)
        Log.d(TAG, "Cached timetable with ${periods.size} periods")
        Success(dto.toDomain(dto.periods.map { it.toDomain() }))
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching timetable", e)
        Error ("Offline — showing cached timetable", e)
    }
}