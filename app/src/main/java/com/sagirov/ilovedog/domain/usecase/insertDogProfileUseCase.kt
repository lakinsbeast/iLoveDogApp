package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.repository.DogsInfoRepository

class insertDogProfileUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(doge: DogsInfoEntity) = repo.insert(doge)
}