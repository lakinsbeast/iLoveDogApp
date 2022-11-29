package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.domain.repository.VaccinationRepository

class insertVaccinationUseCase(
    private val repo: VaccinationRepository
) {
    suspend operator fun invoke(vac: VaccinationsEntity) = repo.insert(vac)
}