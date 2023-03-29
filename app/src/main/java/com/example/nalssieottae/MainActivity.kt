package com.example.nalssieottae

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nalssieottae.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewMainWeather.adapter = WeatherListAdapter(listOf(WeatherItem("오전 7시", R.drawable.ic_cloud, "-4"),
            WeatherItem("오전 9시", R.drawable.ic_sunny, "0")))

        binding.recyclerViewMainWeekWeather.adapter = WeekWeatherListAdapter(listOf(WeatherItem("오전 7시", R.drawable.ic_cloud, "-4"),
            WeatherItem("오전 9시", R.drawable.ic_sunny, "0")))
    }
}