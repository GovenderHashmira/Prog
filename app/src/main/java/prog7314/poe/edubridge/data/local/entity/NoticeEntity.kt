package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import prog7314.poe.edubridge.data.NoticePriority
import java.time.Instant

@Entity(
    tableName = "notices",
    indices = [Index("publishedAt"), Index("isRead"), Index("priority")]
)
data class NoticeEntity(
    @PrimaryKey val noticeId: String,
    val title: String,
    val body: String,
    val audienceRole: String? = null,
    val gradeId: String? = null,
    val priority: NoticePriority = NoticePriority.MEDIUM,
    val publishedAt: Instant,
    val expiresAt: Instant? = null,
    val isRead: Boolean = false
)