package prog7314.poe.edubridge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val userId: String,
    val language: String,
    val notificationEnabled: Boolean,
    val biometricEnabled: Boolean,
    val darkModeEnabled: Boolean,
    val weatherCity: String,
    val updatedAt: Long
)