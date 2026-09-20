package prog7314.poe.edubridge.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.preferences.UserPreferences
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.data.remote.dto.SettingsDto

/**
 * Unit tests for SettingsRepository.
 * Verifies local persistence and remote sync of user preferences.
 */
class SettingsRepositoryTest {

    private lateinit var api: EduBridgeApi
    private lateinit var prefs: UserPreferences
    private lateinit var repository: SettingsRepository

    @Before
    fun setup() {
        api = mockk(relaxed = true)
        prefs = mockk(relaxed = true)
        repository = SettingsRepository(api, prefs)
    }

    // Test 48 — update persists to DataStore
    @Test
    fun `update persists settings to DataStore`() = runTest {
        // Given
        val newSettings = SettingsDto(
            userId = "u-1",
            language = "zu",
            notificationsEnabled = false,
            biometricEnabled = true,
            updatedAt = ""
        )
        coEvery { api.updateSettings(any(), any()) } returns newSettings

        // When
        repository.updateSettings("Bearer ebr-token", newSettings)

        // Then
        coVerify { prefs.saveLanguage("zu") }
        coVerify { prefs.saveNotificationsEnabled(false) }
        coVerify { prefs.saveBiometricEnabled(true) }
    }
}