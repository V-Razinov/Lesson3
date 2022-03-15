package com.example.lesson3

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.lesson3.data.net.retrofit.RetrofitService
import com.example.lesson3.data.net.retrofit.WeatherApi
import com.example.lesson3.other.navigation.Router

//Данный класс необходимо указать в AndroidManifest:
//<application
//        android:name=".App"
//        ...
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        router = Router()
        weatherService = RetrofitService.coroutinesWeatherApi
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
        lateinit var router: Router
            private set
        lateinit var weatherService: WeatherApi
            private set
    }
}