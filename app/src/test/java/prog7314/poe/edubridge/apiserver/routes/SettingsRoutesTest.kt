package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import prog7314.poe.edubridge.apiserver.models.ApiUserSettings
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

// Unit tests for user settings persistence.

class SettingsRoutesTest {

    @Before
    fun setup() {
        ApiDatabase.settings.clear()
        ApiDatabase.settings["u-1"] = ApiUserSettings("u-1", "en", true, false)
    }

    @Test
    fun `default settings returned for known user`() {
        val s = ApiDatabase.settings["u-1"]
        assertThat(s).isNotNull()
        assertThat(s!!.language).isEqualTo("en")
        assertThat(s.notificationsEnabled).isTrue()
    }

    @Test
    fun `updating settings persists changes`() {
        val updated = ApiUserSettings("u-1", "zu", false, true)
        ApiDatabase.settings["u-1"] = updated

        val result = ApiDatabase.settings["u-1"]
        assertThat(result!!.language).isEqualTo("zu")
        assertThat(result.notificationsEnabled).isFalse()
        assertThat(result.biometricEnabled).isTrue()
    }

    @Test
    fun `unknown user returns default`() {
        val s = ApiDatabase.settings["u-99"] ?: ApiUserSettings("u-99")
        assertThat(s.language).isEqualTo("en")
    }
}