package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import prog7314.poe.edubridge.apiserver.*
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

// Unit tests for authentication logic.

class AuthRoutesTest {

    @Before
    fun setup() {
        AuthTokenStore.clear()
    }

    @Test
    fun `valid parent credentials resolve user`() {
        val user = ApiDatabase.users.firstOrNull {
            it.email == "sarah@edubridge.com" && it.password == "password123"
        }
        assertThat(user).isNotNull()
        assertThat(user!!.role).isEqualTo("Parent")
    }

    @Test
    fun `invalid password returns null`() {
        val user = ApiDatabase.users.firstOrNull {
            it.email == "sarah@edubridge.com" && it.password == "wrong"
        }
        assertThat(user).isNull()
    }

    @Test
    fun `issuing token stores user and role`() {
        val token = AuthTokenStore.issueToken("u-1", "Parent")
        assertThat(AuthTokenStore.userIdFor(token)).isEqualTo("u-1")
        assertThat(AuthTokenStore.roleFor(token)).isEqualTo("Parent")
    }

    @Test
    fun `token with bearer prefix works`() {
        val token = AuthTokenStore.issueToken("u-2", "Student")
        assertThat(AuthTokenStore.userIdFor("Bearer $token")).isEqualTo("u-2")
    }

    @Test
    fun `revoking token removes it`() {
        val token = AuthTokenStore.issueToken("u-3", "Teacher")
        AuthTokenStore.revoke(token)
        assertThat(AuthTokenStore.userIdFor(token)).isNull()
    }

    @Test
    fun `blank token returns null`() {
        assertThat(AuthTokenStore.userIdFor(null)).isNull()
        assertThat(AuthTokenStore.userIdFor("")).isNull()
    }
}
