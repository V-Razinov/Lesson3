package com.example.lesson3.data.net.retrofit

import com.example.lesson3.other.BASE_URL
import com.example.lesson3.other.TIMEOUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    val rxWeatherApi = getRetrofit(BASE_URL).create(RxWeatherApi::class.java)
    val coroutinesWeatherApi = getRetrofit(BASE_URL).create(CoroutinesWeatherApi::class.java)

    private fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getClient() = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY// чтобы в логах отображалось
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("Authorization", "Bearer my_token")
            chain.proceed(builder.build())
        })
        .build()
}