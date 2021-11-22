package com.example.lesson3.domain

import android.content.Context
import android.net.ConnectivityManager
import com.example.lesson3.App
import com.example.lesson3.data.models.WeatherErrorResponse
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.data.net.retrofit.RetrofitService
import com.example.lesson3.domain.mappers.EntityToModelMapper
import com.example.lesson3.presentation.BaseItemAdapterItem
import com.example.lesson3.presentation.NotFoundError
import com.google.gson.Gson
import io.reactivex.Observable
import retrofit2.Response
import java.lang.Exception

class RxWeatherInteractor(
    private val mapper: EntityToModelMapper<WeatherSuccessResponse, List<BaseItemAdapterItem>>,
    private val errorMapper: EntityToModelMapper<WeatherErrorResponse, NotFoundError>,
) {
    fun execute(cityName: String): Observable<Result> {
        if (!isNetworkAvailable())
            return Observable.just(Result.Error.NoNetwork)

        return RetrofitService
            .rxWeatherApi
            .loadWeatherByCityName(cityName)
            .map { response -> handleResponse(response, mapper, errorMapper) }
            .onErrorReturn { handleError(it) }
    }
}

class CoroutinesWeatherInteractor(
    private val mapper: EntityToModelMapper<WeatherSuccessResponse, List<BaseItemAdapterItem>>,
    private val errorMapper: EntityToModelMapper<WeatherErrorResponse, NotFoundError>,
) {
    suspend fun execute(cityName: String): Result {
        if (!isNetworkAvailable())
            return Result.Error.NoNetwork

        val response = try {
            RetrofitService
                .coroutinesWeatherApi
                .loadWeatherByCityName(cityName)
        } catch (e: Exception) {
            return handleError(e)
        }
        return handleResponse(response, mapper, errorMapper)
    }
}

sealed class Result {

    data class Success(val items: List<BaseItemAdapterItem>) : Result()

    sealed class Error : Result() {
        data class NotFound(val error: NotFoundError) : Result.Error()
        data class Unknown(val error: String) : Result.Error()
        object NoNetwork : Result.Error()
    }
}

private fun handleResponse(
    response: Response<WeatherSuccessResponse>,
    mapper: EntityToModelMapper<WeatherSuccessResponse, List<BaseItemAdapterItem>>,
    errorMapper: EntityToModelMapper<WeatherErrorResponse, NotFoundError>
): Result {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body == null) {
            try {
                Result.Error.Unknown("Empty body")
            } catch (e: Exception) {
                handleError(e)
            }
        } else {
            try {
                Result.Success(mapper.mapEntity(body))
            } catch (e: Exception) {
                handleError(e)
            }
        }
    } else {
        handleResponseError(response, errorMapper)
    }
}

private fun handleResponseError(
    response: Response<*>,
    errorMapper: EntityToModelMapper<WeatherErrorResponse, NotFoundError>
): Result.Error {
    return when (response.code()) {
        404 -> Result.Error.NotFound(
            error = errorMapper.mapEntity(
                Gson().fromJson(response.errorBody()!!.string(), WeatherErrorResponse::class.java)
            )
        )
        else -> Result.Error.Unknown(
            error = response.errorBody()?.string() ?: "Unknown Error"
        )
    }
}

private fun handleError(e: Throwable): Result.Error {
    return when (e) {
        //todo
        else -> Result.Error.Unknown(
            error = e.localizedMessage ?: e.message ?: "Unknown Error"
        )
    }
}

private fun isNetworkAvailable(): Boolean {
    val cm = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetwork != null
}
