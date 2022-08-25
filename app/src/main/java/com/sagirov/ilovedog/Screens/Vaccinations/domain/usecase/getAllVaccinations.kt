package com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase

import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository
import kotlinx.coroutines.flow.Flow

class getAllVaccinations(
    private val repo: VaccinationRepository
) {
    operator fun invoke(): Flow<MutableList<VaccinationsEntity>> = repo.getAllVaccination()
}