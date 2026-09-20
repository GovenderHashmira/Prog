package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.local.dao.MessageDao
import prog7314.poe.edubridge.data.local.dao.NoticeDao
import prog7314.poe.edubridge.data.mapper.toDomain
import prog7314.poe.edubridge.data.mapper.toEntity
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.domain.model.Message
import prog7314.poe.edubridge.domain.model.Notice
import prog7314.poe.edubridge.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunicationRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val noticeDao: NoticeDao,
    private val messageDao: MessageDao
) {
    private companion object { const val TAG = "EduBridge-Repo-Comm" }

    // ── Notices ──────────────────────────────────────────
    fun observeNotices(): Flow<List<Notice>> =
        noticeDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeUnreadNoticeCount(): Flow<Int> = noticeDao.observeUnreadCount()

    suspend fun refreshNotices(): Resource<List<Notice>> = try {
        Log.d(TAG, "Fetching notices")
        val dtos = api.getNotices()
        noticeDao.upsertAll(dtos.map { it.toEntity() })
        Log.d(TAG, "Cached ${dtos.size} notices")
        Resource.Success(dtos.map { it.toDomain() })
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching notices", e)
        Resource.Error("Offline — showing cached notices", e)
    }

    suspend fun markNoticeRead(noticeId: String) {
        Log.d(TAG, "Marking notice read: $noticeId")
        noticeDao.markRead(noticeId)
    }

    // ── Messages ─────────────────────────────────────────
    fun observeInbox(): Flow<List<Message>> =
        messageDao.observeInbox().map { list -> list.map { it.toDomain() } }

    fun observeUnreadMessageCount(): Flow<Int> = messageDao.observeUnreadCount()

    fun observeMessage(messageId: String): Flow<Message?> =
        messageDao.observeById(messageId).map { it?.toDomain() }

    suspend fun refreshMessages(): Resource<List<Message>> = try {
        Log.d(TAG, "Fetching messages")
        val dtos = api.getMessages()
        messageDao.upsertAll(dtos.map { it.toEntity() })
        Log.d(TAG, "Cached ${dtos.size} messages")
        Resource.Success(dtos.map { it.toDomain() })
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching messages", e)
        Resource.Error("Offline — showing cached messages", e)
    }

    suspend fun markMessageRead(messageId: String) {
        Log.d(TAG, "Marking message read: $messageId")
        messageDao.markRead(messageId, Instant.now().toString())
    }
}