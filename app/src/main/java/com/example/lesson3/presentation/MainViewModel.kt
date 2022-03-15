package com.example.lesson3.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.lesson3.App
import com.example.lesson3.presentation.weatherlist.WeatherFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class MainViewModel : ViewModel() {

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val router = App.router

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    fun initRouter(fm: FragmentManager, containerId: Int, finish: () -> Unit) {
        router.init(fm, containerId, finish)
    }

    fun startWeatherScreen() {
        router.navigateTo(WeatherFragment())
    }

    fun onBackPressed() {
        router.back()
    }
}