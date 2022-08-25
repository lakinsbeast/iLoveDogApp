package com.sagirov.ilovedog.Screens.Vaccinations.data.repoimpl

import com.sagirov.ilovedog.Screens.Vaccinations.data.dao.VaccinationDAO
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.domain.repo.VaccinationRepository
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