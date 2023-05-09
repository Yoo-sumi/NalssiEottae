package com.example.nalssieottae

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("forecast?appid=${BuildConfig.WEATHER_API_KEY}")
    suspend fun getWeather(
        @Query("lat") lat : String,
        @Query("lon") lon : String
    ) : Response<WeeklyWeather>

}