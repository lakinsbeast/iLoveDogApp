package com.sagirov.ilovedog.Screens.DetailedDog.domain.repository

import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import kotlinx.coroutines.flow.Flow

interface DogsEncyclopediaRepository {
    fun getAllDogs(): Flow<MutableList<DogsBreedEncyclopediaEntity>>

    suspend fun getDogById(id: Int): DogsBreedEncyclopediaEntity
}