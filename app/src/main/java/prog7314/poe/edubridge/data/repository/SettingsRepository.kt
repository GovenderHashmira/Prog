package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.local.dao.SettingsDao
import prog7314.poe.edubridge.data.mapper.toDomain
import prog7314.poe.edubridge.data.mapper.toDto
import prog7314.poe.edubridge.data.mapper.toEntity
import prog7314.poe.edubridge.data.preferences.UserPreferences
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.domain.model.Settings
import prog7314.poe.edubridge.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val api: EduBridgeApi,
    private val dao: SettingsDao,
    private val prefs: UserPreferences
) {
    private companion object { const val TAG = "EduBridge-Repo-Settings" }

    fun observe(userId: String): Flow<Settings> =
        dao.observe(userId).map { it?.toDomain() ?: Settings.default(userId) }

    suspend fun get(userId: String): Settings =
        dao.get(userId)?.toDomain() ?: Settings.default(userId)

    suspend fun update(settings: Settings): Resource<Settings> = try {
        Log.d(TAG, "Updating settings for userId=${settings.userId}")
        val dto = api.updateSettings(settings.toDto())
        val entity = dto.toEntity(settings.userId)
        dao.upsert(entity)
        prefs.setLanguage(settings.language)
        prefs.setBiometricEnabled(settings.biometricEnabled)
        Log.d(TAG, "Settings updated")
        Resource.Success(entity.toDomain())
    } catch (e: IOException) {
        Log.e(TAG, "Network failure updating settings", e)
        Resource.Error("Could not save — check connection", e)
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error updating settings", e)
        Resource.Error("Failed to save settings", e)
    }
}