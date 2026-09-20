package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val userId: String,
    val language: String = "en",
    val notificationEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val weatherCity: String = "Johannesburg",
    val updatedAt: Long = System.currentTimeMillis()
)