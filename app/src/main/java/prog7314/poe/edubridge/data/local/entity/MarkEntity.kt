package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "marks",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("subjectId"), Index("academicPeriod")]
)
data class MarkEntity(
    @PrimaryKey val markId: String,
    val studentId: String,
    val subjectId: String,
    val subjectName: String,
    val assessmentName: String,
    val score: Double,
    val academicPeriod: String,
    val updatedAt: Instant
)