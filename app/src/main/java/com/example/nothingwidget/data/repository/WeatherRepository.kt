package com.example.nothingwidget.data.repository

import com.example.nothingwidget.domain.model.HourlyForecast
import com.example.nothingwidget.domain.model.WeatherCondition
import com.example.nothingwidget.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherRepository {
    private val _weatherState = MutableStateFlow(
        WeatherInfo(
            cityName = "London",
            temperatureC = 21,
            condition = WeatherCondition.PARTLY_CLOUDY,
            highC = 24,
            lowC = 15,
            humidityPercent = 62,
            windSpeedKmh = 12,
            uvIndex = 5,
            precipitationChancePercent = 15,
            forecastHourly = listOf(
                HourlyForecast("12 PM", 21, WeatherCondition.SUNNY),
                HourlyForecast("3 PM", 24, WeatherCondition.PARTLY_CLOUDY),
                HourlyForecast("6 PM", 22, WeatherCondition.CLOUDY),
                HourlyForecast("9 PM", 18, WeatherCondition.CLEAR_NIGHT),
                HourlyForecast("12 AM", 15, WeatherCondition.CLEAR_NIGHT)
            )
        )
    )

    val weatherState: Flow<WeatherInfo> = _weatherState.asStateFlow()

    fun updateCity(cityName: String) {
        val updated = when (cityName.lowercase()) {
            "tokyo" -> _weatherState.value.copy(
                cityName = "Tokyo",
                temperatureC = 28,
                condition = WeatherCondition.SUNNY,
                highC = 30,
                lowC = 22
            )
            "new york" -> _weatherState.value.copy(
                cityName = "New York",
                temperatureC = 18,
                condition = WeatherCondition.RAINY,
                highC = 20,
                lowC = 14
            )
            "berlin" -> _weatherState.value.copy(
                cityName = "Berlin",
                temperatureC = 19,
                condition = WeatherCondition.CLOUDY,
                highC = 22,
                lowC = 13
            )
            else -> _weatherState.value.copy(cityName = cityName)
        }
        _weatherState.value = updated
    }
}
