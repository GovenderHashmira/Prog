package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Unit tests for notices and messages.

class CommunicationRoutesTest {

    @Test
    fun `notices list is not empty`() {
        assertThat(ApiDatabase.notices).isNotEmpty()
    }

    @Test
    fun `notices contain sports day`() {
        assertThat(ApiDatabase.notices.any { it.title.contains("Sports") }).isTrue()
    }

    @Test
    fun `notices contain term 3 exams`() {
        assertThat(ApiDatabase.notices.any { it.title.contains("Term 3") }).isTrue()
    }

    @Test
    fun `message list is not empty`() {
        assertThat(ApiDatabase.messages).isNotEmpty()
    }

    @Test
    fun `message lookup by id works`() {
        val m = ApiDatabase.messages.firstOrNull { it.id == "m-1" }
        assertThat(m).isNotNull()
    }

    @Test
    fun `unknown message id returns null`() {
        val m = ApiDatabase.messages.firstOrNull { it.id == "m-999" }
        assertThat(m).isNull()
    }

    @Test
    fun `messages have read flag`() {
        ApiDatabase.messages.forEach {
            assertThat(it.read).isAnyOf(true, false)
        }
    }
}
