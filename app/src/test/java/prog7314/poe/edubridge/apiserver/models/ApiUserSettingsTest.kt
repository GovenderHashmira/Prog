package prog7314.poe.edubridge.apiserver.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Unit tests for ApiUserSettings defaults.

class ApiUserSettingsTest {

    @Test
    fun `defaults are applied when only user id supplied`() {
        val s = ApiUserSettings(userId = "u-1")
        assertThat(s.language).isEqualTo("en")
        assertThat(s.notificationsEnabled).isTrue()
        assertThat(s.biometricEnabled).isFalse()
        assertThat(s.updatedAt).isEmpty()
    }

    @Test
    fun `copy preserves other fields`() {
        val s = ApiUserSettings("u-1", "en", true, false)
        val updated = s.copy(language = "zu")
        assertThat(updated.userId).isEqualTo("u-1")
        assertThat(updated.language).isEqualTo("zu")
        assertThat(updated.notificationsEnabled).isTrue()
    }

    @Test
    fun `equality works for identical settings`() {
        val a = ApiUserSettings("u-1", "en", true, false)
        val b = ApiUserSettings("u-1", "en", true, false)
        assertThat(a).isEqualTo(b)
    }
}