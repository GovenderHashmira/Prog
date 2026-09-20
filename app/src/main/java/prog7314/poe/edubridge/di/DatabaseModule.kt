package prog7314.poe.edubridge.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import prog7314.poe.edubridge.data.local.EduBridgeDatabase
import prog7314.poe.edubridge.data.local.dao.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EduBridgeDatabase =
        Room.databaseBuilder(
            context,
            EduBridgeDatabase::class.java,
            EduBridgeDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides fun provideUserDao(db: EduBridgeDatabase) = db.userDao()
    @Provides fun provideStudentDao(db: EduBridgeDatabase) = db.studentDao()
    @Provides fun provideMarkDao(db: EduBridgeDatabase) = db.markDao()
    @Provides fun provideAttendanceDao(db: EduBridgeDatabase) = db.attendanceDao()
    @Provides fun provideTimetableDao(db: EduBridgeDatabase) = db.timetableDao()
    @Provides fun provideNoticeDao(db: EduBridgeDatabase) = db.noticeDao()
    @Provides fun provideMessageDao(db: EduBridgeDatabase) = db.messageDao()
    @Provides fun provideSettingsDao(db: EduBridgeDatabase) = db.settingsDao()
    @Provides fun provideSyncOperationDao(db: EduBridgeDatabase) = db.syncOperationDao()
}