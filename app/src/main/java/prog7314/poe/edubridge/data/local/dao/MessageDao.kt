package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.MessageEntity

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY sentAt DESC")
    fun observeInbox(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE readAt IS NULL ORDER BY sentAt DESC")
    fun observeUnread(): Flow<List<MessageEntity>>

    @Query("""
        SELECT * FROM messages
        WHERE subject LIKE '%' || :query || '%'
           OR body    LIKE '%' || :query || '%'
        ORDER BY sentAt DESC
    """)
    fun search(query: String): Flow<List<MessageEntity>>

    @Query("SELECT COUNT(*) FROM messages WHERE readAt IS NULL")
    fun observeUnreadCount(): Flow<Int>

    @Query("SELECT * FROM messages WHERE messageId = :messageId LIMIT 1")
    fun observeById(messageId: String): Flow<MessageEntity?>

    @Query("SELECT * FROM messages WHERE messageId = :messageId LIMIT 1")
    suspend fun getById(messageId: String): MessageEntity?

    @Upsert
    suspend fun upsertAll(messages: List<MessageEntity>)

    @Upsert
    suspend fun upsert(message: MessageEntity)

    @Query("UPDATE messages SET readAt = :readAt WHERE messageId = :messageId")
    suspend fun markRead(messageId: String, readAt: String)

    @Query("UPDATE messages SET readAt = :readAt WHERE readAt IS NULL")
    suspend fun markAllRead(readAt: String)

    @Query("DELETE FROM messages")
    suspend fun clear()
}