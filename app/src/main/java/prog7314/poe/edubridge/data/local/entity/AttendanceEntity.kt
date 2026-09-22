package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import prog7314.poe.edubridge.data.AttendanceStatus
import java.time.Instant

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("date")]
)
data class AttendanceEntity(
    @PrimaryKey val attendanceId: String,
    val studentId: String,
    val date: String,                     // ISO-8601: yyyy-MM-dd
    val status: AttendanceStatus,
    val notes: String? = null,
    val updatedAt: Instant
)