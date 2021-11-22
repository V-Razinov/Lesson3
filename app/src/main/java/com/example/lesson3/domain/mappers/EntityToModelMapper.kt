package com.example.lesson3.domain.mappers

interface EntityToModelMapper<Entity, Model> {
    fun mapEntity(entity: Entity): Model
}