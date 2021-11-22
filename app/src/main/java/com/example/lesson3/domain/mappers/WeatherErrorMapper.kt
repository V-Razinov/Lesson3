package com.example.lesson3.domain.mappers

import com.example.lesson3.data.models.WeatherErrorResponse
import com.example.lesson3.presentation.NotFoundError

class WeatherErrorMapper : EntityToModelMapper<WeatherErrorResponse, NotFoundError> {
    override fun mapEntity(entity: WeatherErrorResponse): NotFoundError {
        return NotFoundError(message = entity.message)
    }
}