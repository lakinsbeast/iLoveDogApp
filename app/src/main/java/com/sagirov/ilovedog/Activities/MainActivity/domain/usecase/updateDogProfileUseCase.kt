package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity

class updateDogProfileUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(doge: DogsInfoEntity) = repo.updateDogProfile(doge)
}