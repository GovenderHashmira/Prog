package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** OpenWeatherMap GET /data/2.5/weather — response body. */
data class WeatherResponseDto(
    @SerializedName("name")    val cityName: String,
    @SerializedName("main")    val main: WeatherMainDto,
    @SerializedName("weather") val weather: List<WeatherConditionDto>,
    @SerializedName("dt")      val timestamp: Long,
    @SerializedName("timezone") val timezoneOffsetSeconds: Int = 0
)

data class WeatherMainDto(
    @SerializedName("temp")       val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity")   val humidity: Int,
    @SerializedName("pressure")   val pressure: Int
)

data class WeatherConditionDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("main")        val main: String,      // "Clouds" | "Rain" | …
    @SerializedName("description") val description: String,
    @SerializedName("icon")        val icon: String       // e.g. "04d"
)