package com.example.nalssieottae

data class WeeklyWeather(
    val cod: String,
    val message: Double,
    val cnt: Double,
    val list: List<Body>,
    val city: City
) {

    data class Body(
        val dt: Long,
        val main: Main,
        val weather: List<Weather>,
        val clouds: Clouds,
        val wind: Wind,
        val visibility: Double,
        val pop: Double,
        val sys: Sys,
        val dt_txt: String
    )

    data class Main(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Double,
        val sea_level: Double,
        val grnd_level: Double,
        val humidity: Double,
        val temp_kf: Double,
    )

    data class Weather(
        val id: Double,
        val main: String,
        val description: String,
        val icon: String
    )

    data class Clouds(
        val all: Double
    )

    data class Wind(
        val speed: Double,
        val deg: Double,
        val gust: Double
    )

    data class Sys(
        val pod: String
    )

    data class City(
        val id: Double,
        val name: String,
        val coord: Coord,
        val country: String,
        val population: Double,
        val timezone: Double,
        val sunrise: Double,
        val sunset: Double
    )

    data class Coord(
        val lat: Double,
        val lon: Double
    )

}
