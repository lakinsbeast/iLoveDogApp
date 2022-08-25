package com.sagirov.ilovedog.Screens.Vaccinations.domain.repo

import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import kotlinx.coroutines.flow.Flow

interface VaccinationRepository {
    fun getAllVaccination(): Flow<MutableList<VaccinationsEntity>>
    suspend fun insert(vac: VaccinationsEntity)
    suspend fun delete(id: Int)
}