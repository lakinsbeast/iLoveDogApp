package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.repository.DogsInfoRepository
import kotlinx.coroutines.flow.Flow

class getAllProfilesUseCase(
    private val repo: DogsInfoRepository
) {
    operator fun invoke(): Flow<MutableList<DogsInfoEntity>> = repo.getAllProfiles()
}