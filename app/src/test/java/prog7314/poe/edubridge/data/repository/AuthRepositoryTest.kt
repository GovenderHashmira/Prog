package prog7314.poe.edubridge.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.dao.UserDao
import prog7314.poe.edubridge.data.preferences.UserPreferences
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.data.remote.dto.AuthDto
import prog7314.poe.edubridge.util.Resource
import java.io.IOException

/**
 * Unit tests for AuthRepository.
 * Verifies login flow, token persistence and error handling.
 */
class AuthRepositoryTest {

    private lateinit var api: EduBridgeApi
    private lateinit var userDao: UserDao
    private lateinit var userPreferences: UserPreferences
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        api = mockk()
        userDao = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)
        repository = AuthRepository(api, userDao, userPreferences)
    }

    // Test 43 — Login success saves token
    @Test
    fun `login success saves token`() = runTest {
        // Given
        val dto = AuthDto(
            token = "ebr-test-token",
            role = "Parent",
            userId = "u-1",
            userName = "Sarah Lewis",
            email = "sarah@edubridge.com"
        )
        coEvery { api.login(any()) } returns dto

        // When
        val result = repository.login("sarah@edubridge.com", "password123")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data.token).isEqualTo("ebr-test-token")
        coVerify { userPreferences.saveToken("ebr-test-token") }
        coVerify { userPreferences.saveRole("Parent") }
    }

    // Test 44 — Login IOException → Resource.Error
    @Test
    fun `login IOException returns Resource Error`() = runTest {
        // Given
        coEvery { api.login(any()) } throws IOException("Network unreachable")

        // When
        val result = repository.login("sarah@edubridge.com", "password123")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val error = (result as Resource.Error).message
        assertThat(error).contains("Network")
    }
}