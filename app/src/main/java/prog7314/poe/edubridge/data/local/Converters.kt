// data/local/Converters.kt
package prog7314.poe.edubridge.data.local

import androidx.room.TypeConverter
import prog7314.poe.edubridge.data.*

class Converters {
    @TypeConverter fun roleToString(v: Role?): String? = v?.name
    @TypeConverter fun stringToRole(v: String?): Role? = v?.let(Role::fromString)

    @TypeConverter fun statusToString(v: AttendanceStatus?): String? = v?.name
    @TypeConverter fun stringToStatus(v: String?): AttendanceStatus? = v?.let(AttendanceStatus::fromString)

    @TypeConverter fun syncToString(v: SyncStatus?): String? = v?.name
    @TypeConverter fun stringToSync(v: String?): SyncStatus? = v?.let(SyncStatus::fromString)

    @TypeConverter fun priorityToString(v: NoticePriority?): String? = v?.name
    @TypeConverter fun stringToPriority(v: String?): NoticePriority? = v?.let(NoticePriority::fromString)
}