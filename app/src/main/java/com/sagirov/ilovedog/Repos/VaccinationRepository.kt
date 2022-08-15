package com.sagirov.ilovedog.Repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DAOs.VaccinationDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccinationRepository @Inject constructor(private val dogsDao: VaccinationDAO){
    fun getAllVaccinations(): LiveData<MutableList<VaccinationsEntity>> = dogsDao.getAllVaccinations()

    suspend fun insertVaccination(vac: VaccinationsEntity) {
        dogsDao.insertVaccination(vac)
    }
    suspend fun deleteVaccination(id: Int) {
        dogsDao.deleteVaccination(id)
    }
}