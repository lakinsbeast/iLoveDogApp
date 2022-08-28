package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository

class updateDogsTime(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int, time: Long) = repo.updateDogsTime(id, time)
}