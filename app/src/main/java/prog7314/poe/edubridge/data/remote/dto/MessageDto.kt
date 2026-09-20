package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/messages — response element. */
data class MessageDto(
    @SerializedName("messageId")   val messageId: String,
    @SerializedName("senderId")    val senderId: String,
    @SerializedName("senderName")  val senderName: String,
    @SerializedName("recipientId") val recipientId: String,
    @SerializedName("subject")     val subject: String,
    @SerializedName("body")        val body: String,
    @SerializedName("sentAt")      val sentAt: String,
    @SerializedName("readAt")      val readAt: String? = null
)