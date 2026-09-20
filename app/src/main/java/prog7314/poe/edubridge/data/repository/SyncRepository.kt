package prog7314.poe.edubridge.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import prog7314.poe.edubridge.data.SyncStatus
import prog7314.poe.edubridge.data.local.dao.SyncOperationDao
import prog7314.poe.edubridge.data.local.entity.SyncOperationEntity
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

@Singleton
class SyncRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val dao: SyncOperationDao
) {
    private companion object { const val TAG = "EduBridge-Repo-Sync" }

    fun observePendingCount(): Flow<Int> = dao.observePendingCount()

    fun observePending(): Flow<List<SyncOperationEntity>> = dao.observePending()

    /** Queue an offline action. Called by other repositories when offline. */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun queue(
        userId: String,
        entityType: String,
        entityId: String,
        operationType: String,
        payloadJson: String
    ) {
        val op = SyncOperationEntity(
            operationId = UUID.randomUUID().toString(),
            userId = userId,
            entityType = entityType,
            entityId = entityId,
            operationType = operationType,
            payloadJson = payloadJson,
            clientTimestamp = Instant.now(),
            syncStatus = SyncStatus.PENDING
        )
        dao.upsert(op)
        Log.d(TAG, "Queued $operationType $entityType/$entityId")
    }

    /** Submit all pending operations to the server. */
    suspend fun syncAll(deviceId: String): Resource<SyncStatusDto> {
        Log.i(TAG, "Starting sync")
        val pending = dao.getPending()
        if (pending.isEmpty()) {
            Log.d(TAG, "No pending operations")
            return Resource.Empty
        }

        val batch = SyncBatchDto(
            deviceId = deviceId,
            clientTimestamp = Instant.now().toString(),
            operations = pending.map { it.toDto() }
        )

        return try {
            val result = api.sync(batch)
            val succeeded = result.succeeded.map { it.operationId }
            val failed = result.failed.map { it.operationId }
            if (succeeded.isNotEmpty()) dao.markSucceeded(succeeded)
            if (failed.isNotEmpty()) dao.markFailed(failed)
            dao.clearSucceeded()
            Log.i(TAG, "Sync done: ${succeeded.size} succeeded, ${failed.size} failed")
            Resource.Success(result)
        } catch (e: IOException) {
            Log.e(TAG, "Network failure during sync", e)
            Resource.Error("Sync failed — will retry", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected sync error", e)
            Resource.Error("Sync failed: ${e.message}", e)
        }
    }

    suspend fun getServerStatus(): Resource<SyncStatusDto> = try {
        Resource.Success(api.getSyncStatus())
    } catch (e: Exception) {
        Log.e(TAG, "Failed to fetch sync status", e)
        Resource.Error("Could not fetch sync status", e)
    }

    private fun SyncOperationEntity.toDto() = SyncOperationDto(
        operationId = operationId,
        entityType = entityType,
        entityId = entityId,
        operationType = operationType,
        payload = payloadJson,
        clientTimestamp = clientTimestamp.toString()
    )
}