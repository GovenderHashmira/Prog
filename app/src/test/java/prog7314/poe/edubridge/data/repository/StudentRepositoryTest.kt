package prog7314.poe.edubridge.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.dao.StudentDao
import prog7314.poe.edubridge.data.local.entity.StudentEntity
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.data.remote.dto.StudentDto

/**
 * Unit tests for StudentRepository.
 * Verifies cache-first reads and refresh-from-network behaviour.
 */
class StudentRepositoryTest {

    private lateinit var api: EduBridgeApi
    private lateinit var dao: StudentDao
    private lateinit var repository: StudentRepository

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repository = StudentRepository(api, dao)
    }

    // Test 45 — observeStudents emits cached data
    @Test
    fun `observeStudents emits cached data from DAO`() = runTest {
        // Given
        val cached = listOf(
            StudentEntity("stu-1", "Liam Lewis", "10A", "sch-1", "STU001"),
            StudentEntity("stu-2", "Amara Lewis", "8B", "sch-1", "STU002")
        )
        every { dao.observeAll() } returns flowOf(cached)

        // When / Then
        repository.observeStudents().test {
            val emission = awaitItem()
            assertThat(emission).hasSize(2)
            assertThat(emission[0].name).isEqualTo("Liam Lewis")
            awaitComplete()
        }
    }

    // Test 46 — refreshStudents upserts into DAO
    @Test
    fun `refreshStudents upserts API data into DAO`() = runTest {
        // Given
        val remote = listOf(
            StudentDto("stu-1", "Liam Lewis", "10A", "sch-1", "STU001", true),
            StudentDto("stu-2", "Amara Lewis", "8B", "sch-1", "STU002", true)
        )
        coEvery { api.getStudents(any()) } returns remote

        // When
        repository.refreshStudents("Bearer ebr-token")

        // Then
        coVerify { dao.upsertAll(any()) }
    }
}