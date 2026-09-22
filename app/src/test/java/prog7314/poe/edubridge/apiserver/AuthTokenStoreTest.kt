package prog7314.poe.edubridge.apiserver

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

// Unit tests for AuthTokenStore.

class AuthTokenStoreTest {

    @Before
    fun setup() {
        AuthTokenStore.clear()
    }

    @Test
    fun `issuing token returns non blank string`() {
        val token = AuthTokenStore.issueToken("u-1", "Parent")
        assertThat(token).isNotEmpty()
        assertThat(token).startsWith("ebr-")
    }

    @Test
    fun `token resolves to issuing user id`() {
        val token = AuthTokenStore.issueToken("u-1", "Parent")
        assertThat(AuthTokenStore.userIdFor(token)).isEqualTo("u-1")
    }

    @Test
    fun `token resolves to issuing role`() {
        val token = AuthTokenStore.issueToken("u-2", "Student")
        assertThat(AuthTokenStore.roleFor(token)).isEqualTo("Student")
    }

    @Test
    fun `bearer prefix is stripped before lookup`() {
        val token = AuthTokenStore.issueToken("u-3", "Teacher")
        assertThat(AuthTokenStore.userIdFor("Bearer $token")).isEqualTo("u-3")
    }

    @Test
    fun `revoked token returns null`() {
        val token = AuthTokenStore.issueToken("u-1", "Parent")
        AuthTokenStore.revoke(token)
        assertThat(AuthTokenStore.userIdFor(token)).isNull()
        assertThat(AuthTokenStore.roleFor(token)).isNull()
    }

    @Test
    fun `null or blank token returns null`() {
        assertThat(AuthTokenStore.userIdFor(null)).isNull()
        assertThat(AuthTokenStore.userIdFor("")).isNull()
        assertThat(AuthTokenStore.userIdFor("   ")).isNull()
    }

    @Test
    fun `clear removes all tokens`() {
        val t1 = AuthTokenStore.issueToken("u-1", "Parent")
        val t2 = AuthTokenStore.issueToken("u-2", "Student")
        AuthTokenStore.clear()
        assertThat(AuthTokenStore.userIdFor(t1)).isNull()
        assertThat(AuthTokenStore.userIdFor(t2)).isNull()
    }

    @Test
    fun `two tokens for same user are independent`() {
        val t1 = AuthTokenStore.issueToken("u-1", "Parent")
        val t2 = AuthTokenStore.issueToken("u-1", "Parent")
        assertThat(t1).isNotEqualTo(t2)
        AuthTokenStore.revoke(t1)
        assertThat(AuthTokenStore.userIdFor(t2)).isEqualTo("u-1")
    }
}