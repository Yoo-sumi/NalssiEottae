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
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 뷰모델 생성
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var binding : ActivityMainBinding

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
                Log.d("getWeatherTest", getDate(body.dt))
            }
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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                Log.d("getWeatherTest", "${location.latitude}") // 위도: 37.4835657
                Log.d("getWeatherTest", "${location.longitude}") // 경도: 127.0190708

                viewModel.getWeather(location.latitude, location.longitude)
            }
        return true
    }

    private fun getDate(timestamp: Long) :String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN)
        val tz = TimeZone.getTimeZone("Asia/Seoul")  // TimeZone에 표준시 설정
        dateFormat.timeZone = tz                 //DateFormat에 TimeZone 설정

        val date = Date(timestamp * 1000)   // 현재 날짜가 담긴 Date 객체 생성
        return dateFormat.format(date)
    }
}