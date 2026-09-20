package prog7314.poe.edubridge.data.repository

import android.util.Log
import prog7314.poe.edubridge.data.remote.WeatherApi
import prog7314.poe.edubridge.data.remote.dto.WeatherResponseDto
import prog7314.poe.edubridge.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val apiKey: String            // provided via Hilt module from BuildConfig
) {
    private companion object {
        const val TAG = "EduBridge-Repo-Weather"
        const val CACHE_TTL_SECONDS = 30L * 60
    }

    private val cache = MutableStateFlow<CachedWeather?>(null)

    private data class CachedWeather(val dto: WeatherResponseDto, val fetchedAt: Instant)

    suspend fun getWeather(city: String): Resource<WeatherResponseDto> {
        val cached = cache.value
        if (cached != null &&
            Instant.now().epochSecond - cached.fetchedAt.epochSecond < CACHE_TTL_SECONDS) {
            Log.d(TAG, "Returning cached weather for $city")
            return Resource.Success(cached.dto)
        }

        return try {
            Log.d(TAG, "Fetching weather for $city")
            val dto = api.getWeather(city = city, apiKey = apiKey)
            cache.value = CachedWeather(dto, Instant.now())
            Resource.Success(dto)
        } catch (e: IOException) {
            Log.e(TAG, "Network failure fetching weather", e)
            cached?.let { Resource.Success(it.dto) }
                ?: Resource.Error("Weather unavailable", e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected weather error", e)
            Resource.Error("Weather unavailable", e)
        }
    }
}