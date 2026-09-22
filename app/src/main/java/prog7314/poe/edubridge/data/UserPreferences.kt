package prog7314.poe.edubridge.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Top-level DataStore delegate. Only ONE declaration per file per name
 * is permitted in Kotlin — must live outside the class.
 */
private val Context.dataStore by preferencesDataStore(name = "edubridge_prefs")

/**
 * Single access point for all persisted user preferences.
 *
 * Two backends:
 *  - [EncryptedSharedPreferences] for tokens (hardware-backed encryption).
 *  - [DataStore] for non-sensitive flags (language, biometric toggle, active student).
 */
@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        const val TAG = "EduBridge-Prefs"

        // EncryptedSharedPreferences
        const val SECURE_FILE = "edubridge_secure"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_DEVICE_ID = "device_id"

        const val DEFAULT_LANGUAGE = "en"
    }

    // ──────────────────────────────────────────────────────
    // DataStore keys
    // ──────────────────────────────────────────────────────
    private object Keys {
        val LANGUAGE              = stringPreferencesKey("language")
        val BIOMETRIC_ENABLED     = booleanPreferencesKey("biometric_enabled")
        val NOTIFICATION_ENABLED  = booleanPreferencesKey("notification_enabled")
        val DARK_MODE_ENABLED     = booleanPreferencesKey("dark_mode_enabled")
        val ACTIVE_STUDENT        = stringPreferencesKey("active_student")
        val WEATHER_CITY          = stringPreferencesKey("weather_city")
        val LAST_SYNC_AT          = stringPreferencesKey("last_sync_at")
    }

    // ──────────────────────────────────────────────────────
    // EncryptedSharedPreferences
    // ──────────────────────────────────────────────────────
    // Emulators often end up with a corrupted keyset (reinstalls, wiped Keystore,
    // restored backups). EncryptedSharedPreferences then throws on first use, which
    // surfaced as "Network unavailable" at login and crashed OkHttp threads.
    // So: try once, on failure wipe the file + master key and retry, and as a last
    // resort fall back to plain private prefs so the app keeps working.
    private val securePrefs: SharedPreferences by lazy { createSecurePrefs() }

    private fun buildEncryptedPrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            SECURE_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun createSecurePrefs(): SharedPreferences {
        try {
            return buildEncryptedPrefs()
        } catch (e: Exception) {
            Log.w(TAG, "Encrypted prefs unreadable, resetting them", e)
        }
        try {
            context.deleteSharedPreferences(SECURE_FILE)
            java.security.KeyStore.getInstance("AndroidKeyStore").apply {
                load(null)
                deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            }
            return buildEncryptedPrefs()
        } catch (e: Exception) {
            Log.e(TAG, "Encrypted prefs unavailable, using plain prefs fallback", e)
        }
        return context.getSharedPreferences("${SECURE_FILE}_fallback", Context.MODE_PRIVATE)
    }

    // ──────────────────────────────────────────────────────
    // Tokens
    // ──────────────────────────────────────────────────────

    fun saveAccessToken(token: String) {
        Log.d(TAG, "Saving access token (length=${token.length})")
        securePrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    suspend fun getAccessToken(): String? =
        securePrefs.getString(KEY_ACCESS_TOKEN, null)

    fun saveRefreshToken(token: String) {
        Log.d(TAG, "Saving refresh token")
        securePrefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    suspend fun getRefreshToken(): String? =
        securePrefs.getString(KEY_REFRESH_TOKEN, null)

    fun clearTokens() {
        Log.i(TAG, "Clearing tokens")
        securePrefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    // ──────────────────────────────────────────────────────
    // Device ID
    // ──────────────────────────────────────────────────────

    fun getOrCreateDeviceId(): String {
        securePrefs.getString(KEY_DEVICE_ID, null)?.let { return it }
        val newId = UUID.randomUUID().toString()
        securePrefs.edit().putString(KEY_DEVICE_ID, newId).apply()
        Log.d(TAG, "Generated device ID: $newId")
        return newId
    }

    // ──────────────────────────────────────────────────────
    // Language
    // ──────────────────────────────────────────────────────

    val languageFlow: Flow<String> = context.dataStore.data
        .map { it[Keys.LANGUAGE] ?: DEFAULT_LANGUAGE }

    suspend fun getLanguage(): String = languageFlow.first()

    suspend fun setLanguage(code: String) {
        Log.d(TAG, "Set language: $code")
        context.dataStore.edit { it[Keys.LANGUAGE] = code }
    }

    // ──────────────────────────────────────────────────────
    // Biometric toggle
    // ──────────────────────────────────────────────────────

    val biometricFlow: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.BIOMETRIC_ENABLED] ?: false }

    suspend fun isBiometricEnabled(): Boolean = biometricFlow.first()

    suspend fun setBiometricEnabled(enabled: Boolean) {
        Log.d(TAG, "Set biometric: $enabled")
        context.dataStore.edit { it[Keys.BIOMETRIC_ENABLED] = enabled }
    }

    // ──────────────────────────────────────────────────────
    // Notifications toggle
    // ──────────────────────────────────────────────────────

    val notificationsFlow: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.NOTIFICATION_ENABLED] ?: true }

    suspend fun areNotificationsEnabled(): Boolean = notificationsFlow.first()

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        Log.d(TAG, "Set notifications: $enabled")
        context.dataStore.edit { it[Keys.NOTIFICATION_ENABLED] = enabled }
    }

    // ──────────────────────────────────────────────────────
    // Dark mode
    // ──────────────────────────────────────────────────────

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.DARK_MODE_ENABLED] ?: false }

    suspend fun isDarkModeEnabled(): Boolean = darkModeFlow.first()

    suspend fun setDarkModeEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_MODE_ENABLED] = enabled }
    }

    // ──────────────────────────────────────────────────────
    // Active student
    // ──────────────────────────────────────────────────────

    val activeStudentFlow: Flow<String?> = context.dataStore.data
        .map { it[Keys.ACTIVE_STUDENT] }

    suspend fun getActiveStudent(): String? = activeStudentFlow.first()

    suspend fun setActiveStudent(studentId: String) {
        Log.d(TAG, "Set active student: $studentId")
        context.dataStore.edit { it[Keys.ACTIVE_STUDENT] = studentId }
    }

    suspend fun clearActiveStudent() {
        context.dataStore.edit { it.remove(Keys.ACTIVE_STUDENT) }
    }

    // ──────────────────────────────────────────────────────
    // Weather city
    // ──────────────────────────────────────────────────────

    val weatherCityFlow: Flow<String> = context.dataStore.data
        .map { it[Keys.WEATHER_CITY] ?: "Johannesburg" }

    suspend fun getWeatherCity(): String = weatherCityFlow.first()

    suspend fun setWeatherCity(city: String) {
        context.dataStore.edit { it[Keys.WEATHER_CITY] = city }
    }

    // ──────────────────────────────────────────────────────
    // Last sync timestamp
    // ──────────────────────────────────────────────────────

    val lastSyncFlow: Flow<String?> = context.dataStore.data
        .map { it[Keys.LAST_SYNC_AT] }

    suspend fun getLastSyncAt(): String? = lastSyncFlow.first()

    suspend fun setLastSyncAt(isoTimestamp: String) {
        context.dataStore.edit { it[Keys.LAST_SYNC_AT] = isoTimestamp }
    }

    // ──────────────────────────────────────────────────────
    // Session lifecycle
    // ──────────────────────────────────────────────────────

    fun clearSession() {
        Log.i(TAG, "Clearing session")
        clearTokens()
    }

    suspend fun clearAll() {
        Log.w(TAG, "Full preferences reset")
        clearTokens()
        context.dataStore.edit { it.clear() }
    }
}