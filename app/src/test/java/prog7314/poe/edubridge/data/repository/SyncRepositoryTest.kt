package prog7314.poe.edubridge.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.dao.SyncOperationDao
import prog7314.poe.edubridge.data.local.entity.SyncOperationEntity
import prog7314.poe.edubridge.data.remote.EduBridgeApi

/**
 * Unit tests for SyncRepository.
 * Verifies offline action queueing behaviour.
 */
class SyncRepositoryTest {

    private lateinit var api: EduBridgeApi
    private lateinit var syncDao: SyncOperationDao
    private lateinit var repository: SyncRepository

    @Before
    fun setup() {
        api = mockk(relaxed = true)
        syncDao = mockk(relaxed = true)
        repository = SyncRepository(api, syncDao)
    }

    // Test 49 — queue inserts PENDING op
    @Test
    fun `queue inserts a PENDING sync operation`() = runTest {
        // Given
        val operationSlot = slot<SyncOperationEntity>()

        // When
        repository.queue(
            entityType = "settings",
            entityId = "u-1",
            operationType = "UPDATE",
            payloadJson = """{"language":"zu"}"""
        )

        // Then
        coVerify { syncDao.insert(capture(operationSlot)) }
        val captured = operationSlot.captured
        assertThat(captured.syncStatus).isEqualTo("PENDING")
        assertThat(captured.entityType).isEqualTo("settings")
        assertThat(captured.operationType).isEqualTo("UPDATE")
    }
}