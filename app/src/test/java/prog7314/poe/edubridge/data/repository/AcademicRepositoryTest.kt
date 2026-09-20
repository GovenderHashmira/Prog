package prog7314.poe.edubridge.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.dao.MarkDao
import prog7314.poe.edubridge.data.remote.EduBridgeApi
import prog7314.poe.edubridge.data.remote.dto.ResultDto

/**
 * Unit tests for AcademicRepository.
 * Focuses on refresh logic (clear + upsert pattern).
 */
class AcademicRepositoryTest {

    private lateinit var api: EduBridgeApi
    private lateinit var markDao: MarkDao
    private lateinit var repository: AcademicRepository

    @Before
    fun setup() {
        api = mockk()
        markDao = mockk(relaxed = true)
        repository = AcademicRepository(api, markDao)
    }

    // Test 47 — refreshMarks clears + upserts
    @Test
    fun `refreshMarks clears existing and upserts new records`() = runTest {
        // Given
        val remoteMarks = listOf(
            ResultDto("res-1", "stu-1", "Mathematics", 82.0, "Term 1 Exam", "2026-Term1", "Excellent", "2026-09-15"),
            ResultDto("res-2", "stu-1", "English", 75.0, "Term 1 Exam", "2026-Term1", "Good", "2026-09-15"),
            ResultDto("res-3", "stu-1", "Science", 79.0, "Term 1 Exam", "2026-Term1", "", "2026-09-15")
        )
        coEvery { api.getResults("stu-1") } returns remoteMarks

        // When
        repository.refreshMarks("stu-1")

        // Then
        coVerify { markDao.deleteByStudent("stu-1") }
        coVerify { markDao.upsertAll(any()) }
    }
}