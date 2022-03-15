package com.example.lesson3.data.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class WeatherSuccessResponse(
    val lat: Double,
    val lng: Double,
    val timezoneOffset: Int,
    val daily: List<Daily>
) {
    data class Daily(
        @SerializedName("dt")
        val dateUnix: Int,//Дата в формате Unix
        @SerializedName("temp")
        val temperature: Temp,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike,
        val weather: List<Weather>,
        val pressure: Int,
        val humidity: Int,
        @SerializedName("wind_speed")
        val windSpeed: Double,
        val clouds: Int,
        val snow: Double?,
    ) {
        val dateFormatted
            get() = Calendar.getInstance().run {
                timeInMillis = dateUnix * 1000L
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(time)
                    .toString()
            }
    }

    data class Temp(
        val min: Double,
        val max: Double,

        @SerializedName("morn")
        val morning: Double,
        val day: Double,
        @SerializedName("eve")
        val evening: Double,
        val night: Double,
    )

    data class FeelsLike(
        val day: Double,
        @SerializedName("morn")
        val morning: Double,
        @SerializedName("eve")
        val evening: Double,
        val night: Double,
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String,
    )
}

data class WeatherErrorResponse(
    val cod: String,
    val message: String,
)