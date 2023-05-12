package com.example.nalssieottae

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherResponse : MutableLiveData<Response<WeeklyWeather>> = MutableLiveData()
    val weatherResponse get() = _weatherResponse
    private val _weeklyWeather : MutableLiveData<HashMap<Int, WeeklyWeather.Body>> = MutableLiveData()
    val weeklyWeather get() = _weeklyWeather
//    val weeklyTempMin = HashMap<Int, MutableList<Double>>()
//    val weeklyTempMax = HashMap<Int, MutableList<Double>>()
    var weeklyTempMax = Array(40) {-1e9}
    var weeklyTempMin = Array(40) {1e9}
    var dateList = mutableSetOf<String>()

    fun getWeather(lat : Double, lon : Double) {
        viewModelScope.launch {
            val response = repository.getWeather(lat.toString(), lon.toString())
//            _weatherResponse.value = response
            val temp = HashMap<Int, WeeklyWeather.Body>()
            Log.d("getWeatherTest", "${response.body()?.list?.size}") // 위도: 37.4835657

            response.body()?.list?.forEach { weather ->
                val date = getDate(weather.dt)
                val index = date.split(" ")[0]
                dateList.add(date.split(" ")[0])
                val indexToInt = index.split("-")[2].toInt()
                temp[indexToInt] = weather
                weeklyTempMax[indexToInt] = max(weeklyTempMax[indexToInt], getCelsius(weather.main.temp_max))
                weeklyTempMin[indexToInt] = min(weeklyTempMin[indexToInt], getCelsius(weather.main.temp_min))
//                if (weeklyTempMax[index].isNullOrEmpty()) {
//                    weeklyTempMax[index] = mutableListOf(getCelsius(weather.main.temp_max))
//                    weeklyTempMin[index] = mutableListOf(getCelsius(weather.main.temp_min))
//                } else {
//                    weeklyTempMin[index]?.add(getCelsius(weather.main.temp_min))
//                    weeklyTempMax[index]?.add(getCelsius(weather.main.temp_max))
//                }
            }
            dateList.sorted()
            _weeklyWeather.value = temp
        }
    }

    private fun getDate(timestamp: Long) :String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN)
        val tz = TimeZone.getTimeZone("Asia/Seoul")  // TimeZone에 표준시 설정
        dateFormat.timeZone = tz                 //DateFormat에 TimeZone 설정

        val date = Date(timestamp * 1000)   // 현재 날짜가 담긴 Date 객체 생성
        return dateFormat.format(date)
    }

    private fun getCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }
}