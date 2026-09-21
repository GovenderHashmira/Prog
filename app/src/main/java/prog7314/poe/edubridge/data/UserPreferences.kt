package prog7314.poe.edubridge.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.get

/**
 * Single access point for all persisted user preferences.
 *
 * Two backends:
 *  - [EncryptedSharedPreferences] for tokens (hardware-backed encryption).
 *  - [DataStore] for non-sensitive flags (language, biometric toggle, active student).
 *
 * Every method is safe to call from any dispatcher; DataStore handles its own IO.
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

        // DataStore keys
        const val DATASTORE_NAME = "edubridge_prefs"
        const val DEFAULT_LANGUAGE = "en"
    }

    // ──────────────────────────────────────────────────────
    // DataStore — non-sensitive preferences
    // ──────────────────────────────────────────────────────
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
        val ACTIVE_STUDENT = stringPreferencesKey("active_student")
        val WEATHER_CITY = stringPreferencesKey("weather_city")
        val LAST_SYNC_AT = stringPreferencesKey("last_sync_at")
    }

    // ──────────────────────────────────────────────────────
    // EncryptedSharedPreferences — tokens
    // ──────────────────────────────────────────────────────
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val securePrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            SECURE_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ──────────────────────────────────────────────────────
    // Tokens (synchronous — wrapped by interceptor's runBlocking)
    // ──────────────────────────────────────────────────────

    fun saveAccessToken(token: String) {
        Log.d(TAG, "Saving access token (length=${token.length})")
        securePrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    /**
     * Suspend version so repositories can read on the IO dispatcher.
     * The interceptor calls this inside `runBlocking` — that's fine because
     * EncryptedSharedPreferences reads are in-memory after the first load.
     */
    suspend fun getAccessToken(): String? {
        return securePrefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        Log.d(TAG, "Saving refresh token")
        securePrefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    suspend fun getRefreshToken(): String? {
        return securePrefs.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        Log.i(TAG, "Clearing tokens")
        securePrefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    // ──────────────────────────────────────────────────────
    // Device ID (persistent across sessions)
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
    // Active student (for multi-child parents)
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

    /**
     * Called on logout — clears auth tokens and per-user preferences.
     * Does NOT clear language (user preference survives re-login).
     */
    fun clearSession() {
        Log.i(TAG, "Clearing session")
        clearTokens()
    }

    /**
     * Full reset — used when the user explicitly deletes all local data.
     */
    suspend fun clearAll() {
        Log.w(TAG, "Full preferences reset")
        clearTokens()
        context.dataStore.edit { it.clear() }
    }
}