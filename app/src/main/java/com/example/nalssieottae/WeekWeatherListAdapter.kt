package com.example.nalssieottae

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nalssieottae.databinding.ItemWeekWeatherBinding

class WeekWeatherListAdapter(
    private var weatherList: List<WeeklyWeatherItem> = emptyList()
) : RecyclerView.Adapter<WeekWeatherListAdapter.WeekWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWeatherViewHolder {
        val binding: ItemWeekWeatherBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_week_weather,
            parent,
            false
        )
        return WeekWeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: WeekWeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    class WeekWeatherViewHolder(private val binding: ItemWeekWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weeklyWeatherItem: WeeklyWeatherItem) {
            binding.weatherItem = weeklyWeatherItem
        }
    }
}