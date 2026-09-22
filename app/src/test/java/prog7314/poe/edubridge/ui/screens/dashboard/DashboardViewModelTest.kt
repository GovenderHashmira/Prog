package prog7314.poe.edubridge.ui.screens.dashboard

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.entity.StudentEntity
import prog7314.poe.edubridge.data.repository.StudentRepository
import prog7314.poe.edubridge.util.Resource
import java.io.IOException

/**
 * Unit tests for DashboardViewModel.
 * Verifies loading, success and error state transitions.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: StudentRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    // Test 50 — load → Success
    @Test
    fun `load emits Loading then Success when students available`() = runTest {
        // Given
        val students = listOf(
            StudentEntity("stu-1", "Liam Lewis", "10A", "sch-1", "STU001"),
            StudentEntity("stu-2", "Amara Lewis", "8B", "sch-1", "STU002")
        )
        every { repository.observeStudents() } returns flowOf(students)
        viewModel = DashboardViewModel(repository)

        // When / Then
        viewModel.state.test {
            val initial = awaitItem()
            assertThat(initial).isInstanceOf(DashboardState.Loading::class.java)

            val loaded = awaitItem()
            assertThat(loaded).isInstanceOf(DashboardState.Success::class.java)
            assertThat((loaded as DashboardState.Success).students).hasSize(2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 51 — load → Error on network fail
    @Test
    fun `load emits Error when network fails`() = runTest {
        // Given
        every { repository.observeStudents() } throws IOException("No connection")
        viewModel = DashboardViewModel(repository)

        // When / Then
        viewModel.state.test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(DashboardState.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(DashboardState.Error::class.java)
            assertThat((second as DashboardState.Error).message).contains("No connection")
            cancelAndIgnoreRemainingEvents()
        }
    }
}