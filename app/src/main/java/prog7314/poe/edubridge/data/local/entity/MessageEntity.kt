package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "messages",
    indices = [Index("sentAt"), Index("readAt")]
)
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val senderId: String,
    val senderName: String,
    val recipientId: String,
    val subject: String,
    val body: String,
    val sentAt: Instant,
    val readAt: Instant? = null
)