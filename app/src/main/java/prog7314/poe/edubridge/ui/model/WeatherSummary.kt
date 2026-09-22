package prog7314.poe.edubridge.ui.model

data class WeatherSummary(
    val city: String,
    val temperatureC: Int,
    val condition: String,
    val highC: Int,
    val lowC: Int
)
