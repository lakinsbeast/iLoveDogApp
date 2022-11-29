package com.sagirov.ilovedog.domain.repository

import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import kotlinx.coroutines.flow.Flow

interface VaccinationRepository {
    fun getAllVaccination(): Flow<MutableList<VaccinationsEntity>>
    suspend fun insert(vac: VaccinationsEntity)
    suspend fun delete(id: Int)
}