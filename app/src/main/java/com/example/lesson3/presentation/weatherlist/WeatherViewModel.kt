package com.example.lesson3.presentation.weatherlist

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.App
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.domain.CoroutinesWeatherInteractor
import com.example.lesson3.domain.CoroutinesWeatherInteractor.Result.Error
import com.example.lesson3.domain.CoroutinesWeatherInteractor.Result.Success
import com.example.lesson3.other.SingleLiveEvent
import kotlinx.coroutines.*

class WeatherViewModel : ViewModel() {

    private val router = App.router

    val weatherItems = MutableLiveData<List<WeatherSuccessResponse.Daily>>()
    val message = SingleLiveEvent<String>()
    val isLoading = MutableLiveData(false)

    private val job = Job()
    //Создаем Scope, который изначально работает в Main потоке
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val latLng = 54.32824 to 48.38657//Ульяновск

    // Вызывается при создании класса
    init {
        loadWeather()
    }

    override fun onCleared() {
        super.onCleared()
        vmScope.cancel()
    }

    @SuppressLint("CheckResult")
    fun loadWeather() {
        vmScope.launch {
            isLoading.value = true
            //Делаем запрос в IO потоке, результат получаем в Main
            val result = withContext(Dispatchers.IO) {
                CoroutinesWeatherInteractor()
                    .execute(lat = latLng.first, lng = latLng.second)
            }
            isLoading.value = false
            handleResult(result)
        }
    }

    //Обрабатываем результат
    private fun handleResult(result: CoroutinesWeatherInteractor.Result) {
        when (result) {
            is Success -> weatherItems.value = result.items.daily
            is Error -> handleError(result)
        }
    }

    private fun handleError(result: Error) {
        message.value = when (result) {
            Error.NoNetwork -> "Нет сети"
            is Error.NotFound -> result.error
            is Error.Unknown -> "Неизветсная ошибка"
        }
    }
}