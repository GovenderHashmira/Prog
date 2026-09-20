package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "class_periods",
    foreignKeys = [
        ForeignKey(
            entity = TimetableEntity::class,
            parentColumns = ["timetableId"],
            childColumns = ["timetableId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("timetableId"), Index("dayOfWeek")]
)
data class ClassPeriodEntity(
    @PrimaryKey val periodId: String,
    val timetableId: String,
    val subjectId: String,
    val subjectName: String,
    val teacherName: String,
    val location: String? = null,
    val dayOfWeek: Int,                    // 1 = Monday … 7 = Sunday
    val startTime: String,                 // HH:mm
    val endTime: String                    // HH:mm
)