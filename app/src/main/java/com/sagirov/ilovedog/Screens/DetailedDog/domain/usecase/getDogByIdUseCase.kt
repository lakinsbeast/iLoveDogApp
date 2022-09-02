package com.sagirov.ilovedog.Screens.DetailedDog.domain.usecase

import com.sagirov.ilovedog.Screens.DetailedDog.domain.repository.DogsEncyclopediaRepository
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity


class getDogByIdUseCase(private val repo: DogsEncyclopediaRepository) {
    suspend operator fun invoke(id: Int): DogsBreedEncyclopediaEntity = repo.getDogById(id = id)
}