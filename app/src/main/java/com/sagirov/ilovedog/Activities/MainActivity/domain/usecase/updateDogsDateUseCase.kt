package com.sagirov.ilovedog.Activities.MainActivity.domain.usecase

import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import java.util.*

class updateDogsDateUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int, date: Date) = repo.updateDogsDate(id, date)
}