package com.example.lesson3.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.domain.CoroutinesWeatherInteractor
import com.example.lesson3.domain.Result
import com.example.lesson3.domain.RxWeatherInteractor
import com.example.lesson3.domain.mappers.WeatherErrorMapper
import com.example.lesson3.domain.mappers.WeatherListItemMapper
import com.example.lesson3.other.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    val weatherItems = MutableLiveData<List<BaseItemAdapterItem>>()
    val message = SingleLiveEvent<String>()
    val isLoading = MutableLiveData(false)

    private val disposableContainer = CompositeDisposable()

    private val job = Job()
    private val vmScope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCleared() {
        super.onCleared()
        disposableContainer.dispose()
        vmScope.cancel()
    }

    @SuppressLint("CheckResult")
    fun loadWeather(city: String) {
        vmScope.launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                CoroutinesWeatherInteractor(
                    mapper = WeatherListItemMapper(),
                    errorMapper = WeatherErrorMapper(),
                )
                    .execute(cityName = city)
            }
            isLoading.value = false
            handleResult(result)
        }

        //----------
        RxWeatherInteractor(
            mapper = WeatherListItemMapper(),
            errorMapper = WeatherErrorMapper(),
        )
            .execute(cityName = city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doAfterTerminate { isLoading.value = false }
            .subscribe { result -> handleResult(result) }
            .let(disposableContainer::add)
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> weatherItems.value = result.items
            is Result.Error -> handleError(result)
        }
    }

    private fun handleError(result: Result.Error) {
        when (result) {
            Result.Error.NoNetwork -> message.value = "Нет сети"
            is Result.Error.NotFound -> message.value = result.error.message
            is Result.Error.Unknown -> message.value = "Неизветсная ошибка"
        }
    }
}