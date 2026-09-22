package prog7314.poe.edubridge.data.model
import java.time.Instant

/**
 * A school announcement. Visibility may be scoped by role and/or grade.
 */
data class Notice(
    val id: String,
    val title: String,
    val body: String,
    val audienceRole: String? = null,
    val gradeId: String? = null,
    val publishedAt: Instant,
    val expiresAt: Instant? = null,
    val isRead: Boolean = false
) {
    val isExpired: Boolean
        get() = expiresAt?.let { it.isBefore(Instant.now()) } == true

    /** True when the notice targets all roles. */
    val isBroadcast: Boolean
        get() = audienceRole == null && gradeId == null
}