package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import prog7314.poe.edubridge.data.Role
import java.time.Instant

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val role: Role,
    val language: String = "en",
    val createdAt: Instant,
    val updatedAt: Instant
)