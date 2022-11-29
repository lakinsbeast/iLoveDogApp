package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import kotlinx.coroutines.flow.Flow

class getAllDogsUseCase(private val repo: DogsEncyclopediaRepository) {
    operator fun invoke(): Flow<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
}