package prog7314.poe.edubridge.ui.screens.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import prog7314.poe.edubridge.data.model.Settings

private val Context.settingsDataStore by preferencesDataStore(name = "edubridge_settings")

class SettingsPreferences(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val BIOMETRIC = booleanPreferencesKey("biometric_enabled")
        val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
        val WEATHER_CITY = stringPreferencesKey("weather_city")
    }

    fun observe(userId: String): Flow<Settings> =
        context.settingsDataStore.data.map { prefs ->
            Settings(
                userId = userId,
                language = prefs[Keys.LANGUAGE] ?: "en",
                notificationEnabled = prefs[Keys.NOTIFICATIONS] ?: true,
                biometricEnabled = prefs[Keys.BIOMETRIC] ?: false,
                darkModeEnabled = prefs[Keys.DARK_MODE] ?: false,
                weatherCity = prefs[Keys.WEATHER_CITY] ?: "Johannesburg"
            )
        }

    suspend fun setLanguage(code: String) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE] = code }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    }

    suspend fun setBiometric(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.BIOMETRIC] = enabled }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun setWeatherCity(city: String) {
        context.settingsDataStore.edit { it[Keys.WEATHER_CITY] = city }
    }
}
