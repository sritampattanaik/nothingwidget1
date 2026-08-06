package com.example.nothingwidget.domain.model

data class WeatherInfo(
    val cityName: String = "London",
    val temperatureC: Int = 22,
    val condition: WeatherCondition = WeatherCondition.PARTLY_CLOUDY,
    val highC: Int = 25,
    val lowC: Int = 16,
    val humidityPercent: Int = 58,
    val windSpeedKmh: Int = 14,
    val uvIndex: Int = 4,
    val precipitationChancePercent: Int = 10,
    val forecastHourly: List<HourlyForecast> = listOf(
        HourlyForecast("12 PM", 22, WeatherCondition.SUNNY),
        HourlyForecast("3 PM", 25, WeatherCondition.PARTLY_CLOUDY),
        HourlyForecast("6 PM", 23, WeatherCondition.SUNNY),
        HourlyForecast("9 PM", 19, WeatherCondition.CLEAR_NIGHT),
        HourlyForecast("12 AM", 17, WeatherCondition.CLEAR_NIGHT)
    )
)

enum class WeatherCondition(val label: String, val dotSymbol: String) {
    SUNNY("Sunny", "☀"),
    CLEAR_NIGHT("Clear", "☽"),
    PARTLY_CLOUDY("Partly Cloudy", "⛅"),
    CLOUDY("Cloudy", "☁"),
    RAINY("Rain", "🌧"),
    THUNDERSTORM("Thunderstorm", "⚡"),
    SNOWY("Snow", "❄"),
    FOGGY("Fog", "≡")
}

data class HourlyForecast(
    val timeLabel: String,
    val tempC: Int,
    val condition: WeatherCondition
)
