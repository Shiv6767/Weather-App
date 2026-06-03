package com.example.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val temperature: Double = 0.0,
    val feelsLike: Double = 0.0,
    val aqi: Int = 0,
    val precipitation: Double = 0.0,
    val windSpeed: Double = 0.0,
    val weatherDesc: String = "Loading...",
    val weatherIcon: ImageVector = Icons.Filled.WbSunny,
    val locationName: String = "Locating...",
    val lat: Double? = null,
    val lng: Double? = null
)

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherState())
    val uiState: StateFlow<WeatherState> = _uiState.asStateFlow()

    fun setLocationName(name: String) {
        _uiState.value = _uiState.value.copy(locationName = name)
    }

    fun fetchWeatherForLocation(lat: Double, lng: Double) {
        if (_uiState.value.lat == lat && _uiState.value.lng == lng && !_uiState.value.isLoading) return
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, lat = lat, lng = lng)
        
        viewModelScope.launch {
            try {
                val weatherResponse = NetworkClient.weatherService.getCurrentWeather(lat, lng)
                val aqiResponse = NetworkClient.airQualityService.getCurrentAirQuality(lat, lng)
                
                val current = weatherResponse.current
                val currentAqi = aqiResponse.current
                
                val (desc, icon) = mapWmoCode(current.weatherCode)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    temperature = current.temperature,
                    feelsLike = current.apparentTemperature,
                    aqi = currentAqi.usAqi,
                    precipitation = current.precipitation,
                    windSpeed = current.windSpeed,
                    weatherDesc = desc,
                    weatherIcon = icon
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun mapWmoCode(code: Int): Pair<String, ImageVector> {
        return when (code) {
            0 -> Pair("Sunny", Icons.Filled.WbSunny)
            1, 2, 3 -> Pair("Cloudy", Icons.Filled.Cloud)
            45, 48 -> Pair("Foggy", Icons.Filled.Cloud)
            51, 53, 55, 56, 57 -> Pair("Drizzle", Icons.Filled.WaterDrop)
            61, 63, 65, 66, 67 -> Pair("Rainy", Icons.Filled.WaterDrop)
            71, 73, 75, 77 -> Pair("Snow", Icons.Filled.AcUnit)
            80, 81, 82 -> Pair("Showers", Icons.Filled.WaterDrop)
            95, 96, 99 -> Pair("Thunderstorm", Icons.Filled.FlashOn)
            else -> Pair("Unknown", Icons.Filled.WbCloudy)
        }
    }
}
