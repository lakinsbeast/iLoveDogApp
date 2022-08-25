package com.sagirov.ilovedog.Screens.Vaccinations.repos

import com.sagirov.ilovedog.Screens.Vaccinations.data.dao.VaccinationDAO
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccinationRepository @Inject constructor(private val dogsDao: VaccinationDAO){
    fun getAllVaccinations(): Flow<MutableList<VaccinationsEntity>> = dogsDao.getAllVaccinations()

    suspend fun insertVaccination(vac: VaccinationsEntity) {
        dogsDao.insertVaccination(vac)
    }
    suspend fun deleteVaccination(id: Int) {
        dogsDao.deleteVaccination(id)
    }
}