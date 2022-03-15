package com.example.lesson3.presentation.weatherlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lesson3.R
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.databinding.ItemWeatherBinding
import com.example.lesson3.other.BASE_URL
import com.example.lesson3.other.IMAGE_URL

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val items = mutableListOf<WeatherSuccessResponse.Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherViewHolder(
            binding = ItemWeatherBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<WeatherSuccessResponse.Daily>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: WeatherSuccessResponse.Daily) = with(binding) {
            temperature.text = "${item.temperature.min.toInt()}° / " +
                    "${item.temperature.max.toInt()}°"
            date.text = item.dateFormatted

            val weather = item.weather.getOrNull(0)
            binding.weatherIcon.isVisible = weather != null

            binding.weatherDescription.isVisible = weather != null
            binding.weatherDescription.text = weather?.description
                ?.replaceFirstChar { it.uppercase() }
                ?: ""

            val weatherIcon = weather?.icon
            if (weatherIcon != null) {
                Glide
                    .with(binding.root)
                    .load(IMAGE_URL + "/img/wn/${weatherIcon}@2x.png")
                    .placeholder(R.drawable.ic_weather_placeholder)
                    .into(binding.weatherIcon)
            }
        }
    }

}