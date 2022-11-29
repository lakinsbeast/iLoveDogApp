package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow

class getAllVaccinationsUseCase(
    private val repo: VaccinationRepository
) {
    operator fun invoke(): Flow<MutableList<VaccinationsEntity>> = repo.getAllVaccination()
}