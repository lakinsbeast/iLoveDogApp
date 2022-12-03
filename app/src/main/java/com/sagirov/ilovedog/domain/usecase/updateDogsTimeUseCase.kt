package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DogsInfoRepository

class updateDogsTimeUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int, time: Long) = repo.updateDogsTime(id, time)
}