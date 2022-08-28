package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import kotlinx.coroutines.flow.Flow

class getAllProfiles(
    private val repo: DogsInfoRepository
) {
    operator fun invoke(): Flow<MutableList<DogsInfoEntity>> = repo.getAllProfiles()
}