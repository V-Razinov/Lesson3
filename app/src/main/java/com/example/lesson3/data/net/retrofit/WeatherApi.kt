package com.example.lesson3.data.net.retrofit

import com.example.lesson3.data.models.WeatherSuccessResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(
        "/data/2.5/onecall?" +
                "&appid=84cd0349b6ef847974b3a2bee37dc009" +
                "&exclude=hourly,minutely,alerts,current&units=metric" +
                "&lang=ru"
    )
    suspend fun loadWeatherByCityName(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
    ): Response<WeatherSuccessResponse>
}