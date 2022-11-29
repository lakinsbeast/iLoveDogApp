package com.sagirov.ilovedog.data.repoImpl

import com.sagirov.ilovedog.data.dao.VaccinationDAO
import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow

class VaccinationRepositoryImpl(private val dao: VaccinationDAO) : VaccinationRepository {
    override fun getAllVaccination(): Flow<MutableList<VaccinationsEntity>> {
        return dao.getAllVaccinations()
    }

    override suspend fun insert(vac: VaccinationsEntity) {
        dao.insertVaccination(vac)
    }

    override suspend fun delete(id: Int) {
        dao.deleteVaccination(id)
    }

}