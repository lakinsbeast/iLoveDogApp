package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.VaccinationRepository

class deleteVaccinationUseCase(
    private val repo: VaccinationRepository
) {
    suspend operator fun invoke(id: Int) = repo.delete(id)
}