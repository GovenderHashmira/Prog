package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** POST /api/sync — request body. */
data class SyncBatchDto(
    @SerializedName("deviceId")        val deviceId: String,
    @SerializedName("clientTimestamp") val clientTimestamp: String,     // ISO-8601
    @SerializedName("operations")      val operations: List<SyncOperationDto>
)

/** One pending operation inside a `SyncBatchDto`. */
data class SyncOperationDto(
    @SerializedName("operationId")     val operationId: String,
    @SerializedName("entityType")      val entityType: String,          // "Mark", "Attendance", …
    @SerializedName("entityId")        val entityId: String,
    @SerializedName("operationType")   val operationType: String,       // "CREATE" | "UPDATE" | "DELETE"
    @SerializedName("payload")         val payload: String,             // JSON-encoded payload
    @SerializedName("clientTimestamp") val clientTimestamp: String
)

/** POST /api/sync — response body. */
data class SyncResultDto(
    @SerializedName("succeeded") val succeeded: List<SyncOperationResultDto>,
    @SerializedName("failed")    val failed: List<SyncOperationResultDto>,
    @SerializedName("serverTimestamp") val serverTimestamp: String
)

/** One result inside a `SyncResultDto`. */
data class SyncOperationResultDto(
    @SerializedName("operationId") val operationId: String,
    @SerializedName("status")      val status: String,                  // "SUCCESS" | "FAILED"
    @SerializedName("message")     val message: String? = null
)

/** GET /api/sync/status — response body. */
data class SyncStatusDto(
    @SerializedName("lastSyncAt")    val lastSyncAt: String?,
    @SerializedName("pendingCount")  val pendingCount: Int,
    @SerializedName("serverTimestamp") val serverTimestamp: String
)