package com.example.nalssieottae

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.nalssieottae.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 뷰모델 생성
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var binding : ActivityMainBinding
    private val weekWeatherList = mutableListOf<WeeklyWeatherItem>()
    private val weatherList = mutableListOf<WeatherItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 데이터바인딩
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    Log.d("getWeatherTest", "위치 권한 있음.")
                    getCurrentLocation()
                    // 그냥 진행
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    Log.d("getWeatherTest", "Only approximate location access granted.")

                }
                else -> {
                    Log.d("getWeatherTest", "위치 권한을 부여해주세요.")
                    // 앱 강제 종료
                }
                // No location access granted.
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))


        viewModel.weatherResponse.observe(this) {
            // 날씨 정보 가져옴
            it.body()?.list?.forEach { body ->
                val date = getDate(body.dt)
                val spilt = date.split(" ")
                Log.d("getWeatherTest1", date)
                Log.d("getWeatherTest1", spilt.toString())
            }
        }

        viewModel.weeklyWeather.observe(this) {
            // 날씨 정보 가져옴
            viewModel.dateList.forEach { key ->
                val date = key.split("-")
                it[key.split("-")[2].toInt()]?.forEachIndexed { index, weather ->
                    val icon = weather.weather.get(0).icon
                    if (index == 0) {
                        weekWeatherList.add(WeeklyWeatherItem(doDayOfWeek(it[date[2].toInt()]?.get(0)?.dt), "${date[1]}.${date[2]}", icon, viewModel.weeklyTempMin[date[2].toInt()].toInt().toString(), viewModel.weeklyTempMax[date[2].toInt()].toInt().toString()))
                    }
                    weatherList.add(WeatherItem(getSimpleDate(weather.dt).split(" ")[1], icon, viewModel.getCelsius(weather.main.temp).toInt().toString()))
                }
            }
            binding.recyclerViewMainWeekWeather.adapter = WeekWeatherListAdapter(weekWeatherList)
            binding.recyclerViewMainWeather.adapter = WeatherListAdapter(weatherList)
        }
    }

    private fun getCurrentLocation(): Boolean {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("getWeatherTest", "위치 권한 없음")

            return false
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                if (it == null) {
                    Log.d("getWeatherTest", "Cannot get location")
                }
                else {
                    val lat = it.latitude
                    val lon = it.longitude
                    Log.d("getWeatherTest", "${lat}     ${lon}") // 위도: 37.4835657
                    viewModel.getWeather(it.latitude, it.longitude)
                }
            }
        // 맨 마지막 위치가 없을경우 오류가 발생함
        // https://tristan91.tistory.com/136
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location ->
//                // Got last known location. In some rare situations this can be null.
//                Log.d("getWeatherTest", "${location.latitude}") // 위도: 37.4835657
//                Log.d("getWeatherTest", "${location.longitude}") // 경도: 127.0190708
////
////                viewModel.getWeather(location.latitude, location.longitude)
//            }
        return true
    }

    private fun getDate(timestamp: Long) :String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN)
        val tz = TimeZone.getTimeZone("Asia/Seoul")  // TimeZone에 표준시 설정
        dateFormat.timeZone = tz                 //DateFormat에 TimeZone 설정
        val date = Date(timestamp * 1000)   // 현재 날짜가 담긴 Date 객체 생성
        return dateFormat.format(date)
    }

    private fun getSimpleDate(timestamp: Long) :String {
        val dateFormat = SimpleDateFormat("MM.dd HH", Locale.KOREAN)
        val tz = TimeZone.getTimeZone("Asia/Seoul")  // TimeZone에 표준시 설정
        dateFormat.timeZone = tz                 //DateFormat에 TimeZone 설정
        val date = Date(timestamp * 1000)   // 현재 날짜가 담긴 Date 객체 생성
        return dateFormat.format(date)
    }

    private fun doDayOfWeek(timestamp: Long?): String {
        val cal = Calendar.getInstance()
        timestamp ?: return ""
        cal.time = Date(timestamp * 1000)
        var strWeek = ""
        val nWeek = cal.get(Calendar.DAY_OF_WEEK)

        if (nWeek == 1) {
            strWeek = "일"
        } else if (nWeek == 2) {
            strWeek = "월"
        } else if (nWeek == 3) {
            strWeek = "화"
        } else if (nWeek == 4) {
            strWeek = "수"
        } else if (nWeek == 5) {
            strWeek = "목"
        } else if (nWeek == 6) {
            strWeek = "금"
        } else if (nWeek == 7) {
            strWeek = "토"
        }
        return strWeek
    }
}