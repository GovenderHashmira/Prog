package prog7314.poe.edubridge.data.remote

import androidx.room.Query
import prog7314.poe.edubridge.data.remote.dto.*
import retrofit2.http.*

interface EduBridgeApi {

    @POST("api/auth/sso")
    suspend fun authenticateSso(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): AuthResponse

    @GET("api/users/me")
    suspend fun getCurrentUser(): UserProfileDto

    @GET("api/students")
    suspend fun getLinkedStudents(): List<StudentDto>

    @GET("api/students/{id}/results")
    suspend fun getResults(@Path("id") studentId: String): List<ResultDto>

    @GET("api/students/{id}/attendance")
    suspend fun getAttendance(
        @Path("id") studentId: String,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null
    ): List<AttendanceDto>

    @GET("api/students/{id}/timetable")
    suspend fun getTimetable(
        @Path("id") studentId: String,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null
    ): TimetableDto

    @GET("api/notices")
    suspend fun getNotices(): List<NoticeDto>

    @GET("api/messages")
    suspend fun getMessages(): List<MessageDto>

    @GET("api/messages/{id}")
    suspend fun getMessage(@Path("id") messageId: String): MessageDto

    @POST("api/sync")
    suspend fun sync(@Body batch: SyncBatchDto): SyncResultDto

    @GET("api/sync/status")
    suspend fun getSyncStatus(): SyncStatusDto

    @PUT("api/users/me/settings")
    suspend fun updateSettings(@Body settings: SettingsDto): SettingsDto

    @POST("api/devices/register")
    suspend fun registerDevice(@Body registration: DeviceRegistrationDto)

    @GET("api/schools/{id}")
    suspend fun getSchool(@Path("id") schoolId: String): SchoolDto
}