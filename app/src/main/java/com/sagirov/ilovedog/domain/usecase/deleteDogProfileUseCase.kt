package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DogsInfoRepository

class deleteDogProfileUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}