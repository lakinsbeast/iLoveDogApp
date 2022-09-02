package com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase

import com.sagirov.ilovedog.Screens.DetailedDog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import kotlinx.coroutines.flow.Flow

class getAllDogsUseCase(private val repo: DogsEncyclopediaRepository) {
    operator fun invoke(): Flow<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
}