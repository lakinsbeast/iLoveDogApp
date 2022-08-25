package com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase

import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository

class deleteVaccination(
    private val repo: VaccinationRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}