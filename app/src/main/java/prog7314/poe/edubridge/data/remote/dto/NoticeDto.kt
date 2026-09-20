package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/notices — response element. */
data class NoticeDto(
    @SerializedName("noticeId")     val noticeId: String,
    @SerializedName("title")        val title: String,
    @SerializedName("body")         val body: String,
    @SerializedName("audienceRole") val audienceRole: String? = null,
    @SerializedName("gradeId")      val gradeId: String? = null,
    @SerializedName("priority")     val priority: String = "MEDIUM",   // "HIGH" | "MEDIUM" | "LOW"
    @SerializedName("publishedAt")  val publishedAt: String,
    @SerializedName("expiresAt")    val expiresAt: String? = null,
    @SerializedName("isRead")       val isRead: Boolean = false
)