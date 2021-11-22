package com.example.lesson3.data.net.retrofit

import com.example.lesson3.other.API_KEY
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.other.DEFAULT_LANG
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RxWeatherApi {

    @GET("/data/2.5/weather?&appid=$API_KEY&lang=$DEFAULT_LANG")
    fun loadWeatherByCityName(
        @Query("q") cityName: String
    ): Observable<Response<WeatherSuccessResponse>>
}

interface CoroutinesWeatherApi {

    @GET("/data/2.5/weather?&appid=$API_KEY&lang=$DEFAULT_LANG")
    suspend fun loadWeatherByCityName(
        @Query("q") cityName: String
    ): Response<WeatherSuccessResponse>
}