package prog7314.poe.edubridge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import prog7314.poe.edubridge.data.local.dao.*
import prog7314.poe.edubridge.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        StudentEntity::class,
        MarkEntity::class,
        AttendanceEntity::class,
        TimetableEntity::class,
        ClassPeriodEntity::class,
        NoticeEntity::class,
        MessageEntity::class,
        SettingsEntity::class,
        SyncOperationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EduBridgeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun studentDao(): StudentDao
    abstract fun markDao(): MarkDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun timetableDao(): TimetableDao
    abstract fun noticeDao(): NoticeDao
    abstract fun messageDao(): MessageDao
    abstract fun settingsDao(): SettingsDao
    abstract fun syncOperationDao(): SyncOperationDao

    companion object {
        const val DATABASE_NAME = "edubridge.db"
    }
}