package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.SyncOperationEntity

@Dao
interface SyncOperationDao {

    @Query("""
        SELECT * FROM sync_operations
        WHERE syncStatus = 'PENDING'
        ORDER BY clientTimestamp ASC
    """)
    suspend fun getPending(): List<SyncOperationEntity>

    @Query("""
        SELECT * FROM sync_operations
        WHERE syncStatus = 'PENDING'
        ORDER BY clientTimestamp ASC
    """)
    fun observePending(): Flow<List<SyncOperationEntity>>

    @Query("SELECT COUNT(*) FROM sync_operations WHERE syncStatus = 'PENDING'")
    fun observePendingCount(): Flow<Int>

    @Query("SELECT * FROM sync_operations WHERE operationId = :operationId LIMIT 1")
    suspend fun getById(operationId: String): SyncOperationEntity?

    @Upsert
    suspend fun upsert(operation: SyncOperationEntity)

    @Upsert
    suspend fun upsertAll(operations: List<SyncOperationEntity>)

    @Query("""
        UPDATE sync_operations
        SET syncStatus = :status
        WHERE operationId = :operationId
    """)
    suspend fun updateStatus(operationId: String, status: String)

    @Query("""
        UPDATE sync_operations
        SET syncStatus = 'SUCCESS'
        WHERE operationId IN (:operationIds)
    """)
    suspend fun markSucceeded(operationIds: List<String>)

    @Query("""
        UPDATE sync_operations
        SET syncStatus = 'FAILED'
        WHERE operationId IN (:operationIds)
    """)
    suspend fun markFailed(operationIds: List<String>)

    @Query("DELETE FROM sync_operations WHERE syncStatus = 'SUCCESS'")
    suspend fun clearSucceeded()

    @Query("DELETE FROM sync_operations WHERE syncStatus = 'FAILED'")
    suspend fun clearFailed()

    @Query("DELETE FROM sync_operations")
    suspend fun clear()
}