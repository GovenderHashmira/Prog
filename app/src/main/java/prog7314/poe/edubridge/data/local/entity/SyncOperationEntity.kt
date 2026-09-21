package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import prog7314.poe.edubridge.data.*
import java.time.Instant

@Entity(
    tableName = "sync_operations",
    indices = [Index("syncStatus"), Index("userId")]
)
data class SyncOperationEntity(
    @PrimaryKey val operationId: String,
    val userId: String,
    val entityType: String,
    val entityId: String,
    val operationType: String = OperationType.CREATE.toString(),
    val payloadJson: String,
    val clientTimestamp: Instant,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)