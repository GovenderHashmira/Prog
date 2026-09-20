package prog7314.poe.edubridge.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import prog7314.poe.edubridge.data.AttendanceStatus
import prog7314.poe.edubridge.data.NoticePriority
import prog7314.poe.edubridge.data.Role
import prog7314.poe.edubridge.data.SyncStatus
import java.time.Instant

class Converters {

    @TypeConverter fun instantToString(v: Instant?): String? = v?.toString()
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter fun stringToInstant(v: String?): Instant? = v?.let(Instant::parse)

    @TypeConverter fun roleToString(v: Role?): String? = v?.name
    @TypeConverter fun stringToRole(v: String?): Role? = v?.let(Role::fromString)

    @TypeConverter fun attendanceToString(v: AttendanceStatus?): String? = v?.name
    @TypeConverter fun stringToAttendance(v: String?): AttendanceStatus? =
        v?.let(AttendanceStatus::fromString)

    @TypeConverter fun syncToString(v: SyncStatus?): String? = v?.name
    @TypeConverter fun stringToSync(v: String?): SyncStatus? =
        v?.let(SyncStatus::fromString)

    @TypeConverter fun priorityToString(v: NoticePriority?): String? = v?.name
    @TypeConverter fun stringToPriority(v: String?): NoticePriority? =
        v?.let(NoticePriority::fromString)
}