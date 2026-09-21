package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.SettingsEntity
import prog7314.poe.edubridge.data.model.Settings
import prog7314.poe.edubridge.data.remote.dto.SettingsDto

fun SettingsEntity.toDomain() = Settings(
    userId = userId,
    language = language,
    notificationEnabled = notificationEnabled,
    biometricEnabled = biometricEnabled,
    darkModeEnabled = darkModeEnabled,
    weatherCity = weatherCity,
    updatedAt = updatedAt
)

fun Settings.toEntity() = SettingsEntity(
    userId = userId,
    language = language,
    notificationEnabled = notificationEnabled,
    biometricEnabled = biometricEnabled,
    darkModeEnabled = darkModeEnabled,
    weatherCity = weatherCity,
    updatedAt = updatedAt
)

fun SettingsDto.toEntity(userId: String) = SettingsEntity(
    userId = userId,
    language = language,
    notificationEnabled = notificationEnabled,
    biometricEnabled = biometricEnabled,
    darkModeEnabled = darkModeEnabled,
    weatherCity = weatherCity,
    updatedAt = System.currentTimeMillis()
)

fun Settings.toDto() = SettingsDto(
    language = language,
    notificationEnabled = notificationEnabled,
    biometricEnabled = biometricEnabled,
    darkModeEnabled = darkModeEnabled,
    weatherCity = weatherCity
)