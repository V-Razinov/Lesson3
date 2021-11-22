package com.example.lesson3.data.models

data class WeatherSuccessResponse(
    val id: Int,
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
) {
    data class Coord(
        val lon: Double,
        val lat: Double
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class Main(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val humidity: Int,
    )

    data class Wind(
        val speed: Double,
        val deg: Int,
        val gust: Double,
    )

    data class Clouds(
        val all: Int,
    )
}

data class WeatherErrorResponse(
    val cod: String,
    val message: String,
)