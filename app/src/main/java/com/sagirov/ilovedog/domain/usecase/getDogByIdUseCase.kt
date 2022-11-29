package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity


class getDogByIdUseCase(private val repo: DogsEncyclopediaRepository) {
    suspend operator fun invoke(id: Int): DogsBreedEncyclopediaEntity = repo.getDogById(id = id)
}