package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val current: CurrentWeather
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "temperature_2m") val temperature: Double,
    @Json(name = "relative_humidity_2m") val humidity: Int,
    @Json(name = "apparent_temperature") val apparentTemperature: Double,
    @Json(name = "precipitation") val precipitation: Double,
    @Json(name = "weather_code") val weatherCode: Int,
    @Json(name = "wind_speed_10m") val windSpeed: Double
)

@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    val current: CurrentAirQuality
)

@JsonClass(generateAdapter = true)
data class CurrentAirQuality(
    @Json(name = "us_aqi") val usAqi: Int
)

interface OpenMeteoApiService {
    @GET("v1/forecast?current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): WeatherResponse
}

interface OpenMeteoAirQualityService {
    @GET("v1/air-quality?current=us_aqi")
    suspend fun getCurrentAirQuality(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double
    ): AirQualityResponse
}

object NetworkClient {
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"
    private const val AIR_QUALITY_BASE_URL = "https://air-quality-api.open-meteo.com/"

    val weatherService: OpenMeteoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenMeteoApiService::class.java)
    }

    val airQualityService: OpenMeteoAirQualityService by lazy {
        Retrofit.Builder()
            .baseUrl(AIR_QUALITY_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenMeteoAirQualityService::class.java)
    }
}
