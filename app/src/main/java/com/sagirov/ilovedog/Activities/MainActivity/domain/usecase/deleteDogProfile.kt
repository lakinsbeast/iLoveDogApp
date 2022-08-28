package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository

class deleteDogProfile(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}