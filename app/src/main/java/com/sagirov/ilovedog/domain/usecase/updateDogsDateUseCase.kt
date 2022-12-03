package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.DogsInfoRepository
import java.util.*

class updateDogsDateUseCase(
    private val repo: DogsInfoRepository
) {
    suspend operator fun invoke(id: Int, date: Date) = repo.updateDogsDate(id, date)
}