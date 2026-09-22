package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "students",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("gradeId"), Index("schoolId")]
)
data class StudentEntity(
    @PrimaryKey val studentId: String,
    val userId: String?,
    val schoolId: String,
    val gradeId: String,
    val firstName: String,
    val lastName: String,
    val studentNumber: String,
    val active: Boolean = true
) {
    /** Convenience for UI avatars. */
    val fullName: String get() = "$firstName $lastName"
}