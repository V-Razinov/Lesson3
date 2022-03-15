package com.example.lesson3.domain

import com.example.lesson3.App
import com.example.lesson3.data.models.WeatherErrorResponse
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.data.net.retrofit.RetrofitService
import com.example.lesson3.other.utils.isNetworkAvailable
import com.google.gson.Gson
import retrofit2.Response

class CoroutinesWeatherInteractor {

    suspend fun execute(lat: Double, lng: Double): Result {
        if (!App.context.isNetworkAvailable)
            return Result.Error.NoNetwork

        val response = try {
            RetrofitService
                .coroutinesWeatherApi
                .loadWeatherByCityName(lat = lat, lng = lng)
        } catch (e: Exception) {
            e.printStackTrace()
            return handleError(e)
        }
        return handleResponse(response)
    }

    //Обрабатывает результат запроса
    private fun handleResponse(
        response: Response<WeatherSuccessResponse>,
    ): Result {
        //Если ответ успешный пытваемся выдернуть, иначе обрабатываем ошибку
        return if (response.isSuccessful) {
            val body = response.body()
            if (body == null) {
                try {
                    Result.Error.Unknown("Empty body")
                } catch (e: Exception) {
                    handleError(e)
                }
            } else {
                Result.Success(body)
            }
        } else {
            handleResponseError(response)
        }
    }

    //Обрабатывает серверные ошибки
    private fun handleResponseError(
        response: Response<*>,
    ): Result.Error {
        return when (response.code()) {
            404 -> Result.Error.NotFound(
                Gson().fromJson(
                    response.errorBody()!!.string(),
                    WeatherErrorResponse::class.java
                ).message
            )
            else -> Result.Error.Unknown(
                error = response.errorBody()?.string() ?: "Unknown Error"
            )
        }
    }
    //обрабатывает не серверную ошибку
    private fun handleError(e: Throwable): Result.Error {
        return when (e) {
            //todo
            else -> Result.Error.Unknown(
                error = e.localizedMessage ?: e.message ?: "Unknown Error"
            )
        }
    }

    sealed class Result {

        data class Success(val items: WeatherSuccessResponse) : Result()

        sealed class Error : Result() {
            data class NotFound(val error: String) : Result.Error()
            data class Unknown(val error: String) : Result.Error()
            object NoNetwork : Result.Error()
        }
    }
}
