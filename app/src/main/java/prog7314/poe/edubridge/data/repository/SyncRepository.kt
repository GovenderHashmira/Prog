package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.SyncStatus
import prog7314.poe.edubridge.data.local.dao.SyncOperationDao
import prog7314.poe.edubridge.data.local.entity.SyncOperationEntity
import prog7314.poe.edubridge.data.UserPreferences
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.data.remote.dto.SyncBatchDto
import prog7314.poe.edubridge.data.remote.dto.SyncOperationDto
import prog7314.poe.edubridge.data.remote.dto.SyncStatusDto
import prog7314.poe.edubridge.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Owns the offline-first synchronisation queue.
 *
 * Responsibilities:
 *  - Queue pending mutations (CREATE / UPDATE / DELETE) when offline.
 *  - Submit batches to `POST /api/sync` when online.
 *  - Track per-operation status (PENDING → SUCCESS | FAILED).
 *  - Expose pending count so the UI can show a sync badge.
 */
@Singleton
class SyncRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val dao: SyncOperationDao,
    private val prefs: UserPreferences
) {
    private companion object {
        const val TAG = "EduBridge-Repo-Sync"
        const val MAX_BATCH_SIZE = 50
    }

    // ──────────────────────────────────────────────────────
    // Observability
    // ──────────────────────────────────────────────────────

    fun observePendingCount(): Flow<Int> = dao.observePendingCount()

    fun observePending(): Flow<List<SyncOperationEntity>> = dao.observePending()

    fun observeLastSyncAt(): Flow<String?> = prefs.lastSyncFlow

    // ──────────────────────────────────────────────────────
    // Queueing
    // ──────────────────────────────────────────────────────

    suspend fun queue(
        userId: String,
        entityType: String,
        entityId: String,
        operationType: String,
        payloadJson: String
    ) {
        val operation = SyncOperationEntity(
            operationId = UUID.randomUUID().toString(),
            userId = userId,
            entityType = entityType,
            entityId = entityId,
            operationType = operationType,
            payloadJson = payloadJson,
            clientTimestamp = Instant.now(),
            syncStatus = SyncStatus.PENDING
        )
        dao.upsert(operation)
        Log.d(TAG, "Queued $operationType $entityType/$entityId (op=${operation.operationId})")
    }

    suspend fun clearQueue() {
        Log.i(TAG, "Clearing sync queue")
        dao.clear()
    }

    suspend fun clearSynced() {
        dao.clearSucceeded()
    }

    // ──────────────────────────────────────────────────────
    // Sync execution
    // ──────────────────────────────────────────────────────

    suspend fun syncAll(deviceId: String): Resource<SyncStatusDto> {
        Log.i(TAG, "Starting sync for deviceId=$deviceId")

        val pending = dao.getPending()
        if (pending.isEmpty()) {
            Log.d(TAG, "No pending operations — nothing to sync")
            return Resource.Empty("No pending operations")
        }

        val batches = pending.chunked(MAX_BATCH_SIZE)
        var totalSucceeded = 0
        var totalFailed = 0

        for ((index, batch) in batches.withIndex()) {
            Log.d(TAG, "Submitting batch ${index + 1}/${batches.size} (${batch.size} ops)")
            when (val result = submitBatch(deviceId, batch)) {
                is Resource.Success -> {
                    totalSucceeded += result.data.pendingCount.let { 0 } // see note
                    totalFailed += 0
                }
                is Resource.Error -> {
                    Log.e(TAG, "Batch ${index + 1} failed: ${result.message}")
                    return result
                }
                else -> Unit
            }
        }

        val now = Instant.now().toString()
        prefs.setLastSyncAt(now)
        dao.clearSucceeded()

        Log.i(TAG, "Sync complete: $totalSucceeded succeeded, $totalFailed failed")
        return Resource.Success(
            SyncStatusDto(
                lastSyncAt = now,
                pendingCount = dao.getPending().size,
                serverTimestamp = now
            )
        )
    }

    private suspend fun submitBatch(
        deviceId: String,
        batch: List<SyncOperationEntity>
    ): Resource<SyncStatusDto> {
        val payload = SyncBatchDto(
            deviceId = deviceId,
            clientTimestamp = Instant.now().toString(),
            operations = batch.map { it.toDto() }
        )

        return try {
            val result = api.sync(payload)

            val succeededIds = result.succeeded.map { it.operationId }
            val failedIds = result.failed.map { it.operationId }

            if (succeededIds.isNotEmpty()) {
                dao.markSucceeded(succeededIds)
                Log.d(TAG, "Marked SUCCESS: ${succeededIds.size}")
            }
            if (failedIds.isNotEmpty()) {
                dao.markFailed(failedIds)
                result.failed.forEach { op ->
                    Log.w(TAG, "Operation ${op.operationId} failed: ${op.message ?: "no message"}")
                }
            }

            Resource.Success(
                SyncStatusDto(
                    lastSyncAt = result.serverTimestamp,
                    pendingCount = dao.getPending().size,
                    serverTimestamp = result.serverTimestamp
                )
            )
        } catch (e: IOException) {
            Log.e(TAG, "Network failure during batch sync", e)
            Resource.Error("Offline — sync will retry", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during batch sync", e)
            Resource.Error("Sync error: ${e.message}", e)
        }
    }

    // ──────────────────────────────────────────────────────
    // Server-side status
    // ──────────────────────────────────────────────────────

    suspend fun getServerStatus(): Resource<SyncStatusDto> = try {
        Log.d(TAG, "Fetching server sync status")
        Resource.Success(api.getSyncStatus())
    } catch (e: IOException) {
        Log.e(TAG, "Network failure fetching sync status", e)
        Resource.Error("Could not reach server", e)
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error fetching sync status", e)
        Resource.Error("Could not fetch sync status", e)
    }

    // ──────────────────────────────────────────────────────
    // Retry helpers
    // ──────────────────────────────────────────────────────

    /** Move FAILED operations back to PENDING so they will be retried. */
    suspend fun retryFailed() {
        Log.i(TAG, "Re-queuing FAILED operations for retry")
        dao.requeueFailed()
    }

    /** Delete FAILED operations without retrying. */
    suspend fun discardFailed() {
        Log.w(TAG, "Discarding FAILED operations")
        dao.clearFailed()
    }

    // ──────────────────────────────────────────────────────
    // Mapping
    // ──────────────────────────────────────────────────────

    private fun SyncOperationEntity.toDto() = SyncOperationDto(
        operationId = operationId,
        entityType = entityType,
        entityId = entityId,
        operationType = operationType,
        payload = payloadJson,
        clientTimestamp = clientTimestamp.toString()
    )
}