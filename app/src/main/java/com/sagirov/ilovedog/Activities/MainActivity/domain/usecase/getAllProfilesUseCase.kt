package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import kotlinx.coroutines.flow.Flow

class getAllProfilesUseCase(
    private val repo: DogsInfoRepository
) {
    operator fun invoke(): Flow<MutableList<DogsInfoEntity>> = repo.getAllProfiles()
}