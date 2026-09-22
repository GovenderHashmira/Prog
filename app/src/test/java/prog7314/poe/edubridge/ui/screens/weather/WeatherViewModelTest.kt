package prog7314.poe.edubridge.ui.screens.weather

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import prog7314.poe.edubridge.data.local.entity.WeatherEntity
import prog7314.poe.edubridge.data.repository.WeatherRepository

/**
 * Unit tests for WeatherViewModel.
 * Verifies cache-first behaviour when offline.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    // Test 52 — Returns cached weather offline
    @Test
    fun `returns cached weather when offline`() = runTest {
        // Given
        val cached = WeatherEntity(
            city = "Cape Town",
            temperature = 22.5,
            condition = "Sunny",
            iconCode = "01d",
            updatedAt = "2026-09-20T10:00:00Z"
        )
        coEvery { repository.getWeather(any(), any()) } returns cached

        viewModel = WeatherViewModel(repository)

        // When
        viewModel.load("Cape Town")

        // Then
        viewModel.state.test {
            val loading = awaitItem()
            assertThat(loading).isInstanceOf(WeatherState.Loading::class.java)

            val loaded = awaitItem()
            assertThat(loaded).isInstanceOf(WeatherState.Success::class.java)
            val data = (loaded as WeatherState.Success).weather
            assertThat(data.city).isEqualTo("Cape Town")
            assertThat(data.temperature).isEqualTo(22.5)
            cancelAndIgnoreRemainingEvents()
        }
    }
}