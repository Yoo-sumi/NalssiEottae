package com.example.nalssieottae

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherResponse : MutableLiveData<Response<WeeklyWeather>> = MutableLiveData()
    val weatherResponse get() = _weatherResponse

    fun getWeather(lat : Double, lon : Double) {
        viewModelScope.launch {
            val response = repository.getWeather(lat.toString(), lon.toString())
            _weatherResponse.value = response
        }
    }
}