package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.repository.DogsInfoRepository

class updateDogProfileUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(doge: DogsInfoEntity) = repo.updateDogProfile(doge)
}