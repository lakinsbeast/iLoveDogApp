package com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase

import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository

class insertVaccinationUseCase(
    private val repo: VaccinationRepository
) {
    suspend operator fun invoke(vac: VaccinationsEntity) = repo.insert(vac)
}