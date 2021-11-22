package com.example.lesson3.domain.mappers

import com.example.lesson3.other.IMAGE_URL
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.presentation.BaseItemAdapterItem

class WeatherListItemMapper : EntityToModelMapper<WeatherSuccessResponse, List<BaseItemAdapterItem>> {
    override fun mapEntity(entity: WeatherSuccessResponse): List<BaseItemAdapterItem> {
        return mutableListOf<BaseItemAdapterItem>().apply {
            add(
                BaseItemAdapterItem.Temperature(
                    temp = "Температура: ${entity.main.temp}"
                )
            )
            add(
                BaseItemAdapterItem.Pressure(
                    value = "Давление: ${entity.main.pressure}"
                )
            )
            add(
                BaseItemAdapterItem.Humidity(
                    value = "Влажность: ${entity.main.humidity}"
                )
            )
            add(
                BaseItemAdapterItem.Wind(
                    speed = "Скорость ветра: ${entity.wind.speed} метров в секунду",
                    degree = "Направление ветра: ${entity.wind.deg}°"
                )
            )
            add(
                BaseItemAdapterItem.Clouds(
                    value = "Облачность: ${entity.clouds.all}"
                )
            )
            addAll(
                entity.weather.map {
                    BaseItemAdapterItem.Weather(
                        id = it.id,
                        description = it.description,
                        iconUrl = IMAGE_URL + "/img/wn/${it.icon}@2x.png"
                    )
                }
            )
        }
    }
}