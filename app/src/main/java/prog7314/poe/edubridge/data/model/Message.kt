package prog7314.poe.edubridge.data.model

import java.time.Instant

/**
 * An inbox message received by the current user.
 */
data class Message(
    val id: String,
    val senderName: String,
    val subject: String,
    val body: String,
    val sentAt: Instant,
    val readAt: Instant? = null
) {
    val isUnread: Boolean
        get() = readAt == null

    /** Short preview used by the inbox list. */
    val preview: String
        get() = body.take(80).let { if (body.length > 80) "$it…" else it }
}