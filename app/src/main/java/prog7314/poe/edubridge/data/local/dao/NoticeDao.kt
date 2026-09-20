package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.NoticeEntity

@Dao
interface NoticeDao {

    @Query("""
        SELECT * FROM notices
        ORDER BY
            CASE priority
                WHEN 'HIGH' THEN 1
                WHEN 'MEDIUM' THEN 2
                WHEN 'LOW' THEN 3
            END ASC,
            publishedAt DESC
    """)
    fun observeAll(): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM notices WHERE isRead = 0 ORDER BY publishedAt DESC")
    fun observeUnread(): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM notices WHERE priority = :priority ORDER BY publishedAt DESC")
    fun observeByPriority(priority: String): Flow<List<NoticeEntity>>

    @Query("SELECT COUNT(*) FROM notices WHERE isRead = 0")
    fun observeUnreadCount(): Flow<Int>

    @Query("SELECT * FROM notices WHERE noticeId = :noticeId LIMIT 1")
    suspend fun getById(noticeId: String): NoticeEntity?

    @Upsert
    suspend fun upsertAll(notices: List<NoticeEntity>)

    @Upsert
    suspend fun upsert(notice: NoticeEntity)

    @Query("UPDATE notices SET isRead = 1 WHERE noticeId = :noticeId")
    suspend fun markRead(noticeId: String)

    @Query("UPDATE notices SET isRead = 1")
    suspend fun markAllRead()

    @Query("DELETE FROM notices WHERE expiresAt IS NOT NULL AND expiresAt < :now")
    suspend fun deleteExpired(now: String)

    @Query("DELETE FROM notices")
    suspend fun clear()
}