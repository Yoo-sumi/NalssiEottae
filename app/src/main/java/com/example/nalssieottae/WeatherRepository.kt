package com.example.nalssieottae

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) {

    suspend fun getWeather(lat : String, lon : String) : Response<WeeklyWeather> {
        return weatherService.getWeather(lat, lon)
    }
}