package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsBreedEncyclopediaRepository @Inject constructor(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>> = dogsDao.getAllDogsProfiles()
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>> = dogsDao.getAllDocuments()
    fun getAllVaccinations(): LiveData<MutableList<VaccinationsEntity>> = dogsDao.getAllVaccinations()

    suspend fun insertDocumentFile(file: DocumentsEntity) {
        dogsDao.insertDocumentFile(file)
    }
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>) {
        dogsDao.updateDocumentFile(id, doc)
    }
    suspend fun deleteDocument(id: Int) {
        dogsDao.deleteDocument(id)
    }

    suspend fun insertVaccination(vac: VaccinationsEntity) {
        dogsDao.insertVaccination(vac)
    }
    suspend fun deleteVaccination(id: Int) {
        dogsDao.deleteVaccination(id)
    }

    suspend fun updateDogsTime(id: Int, time: Long){
        dogsDao.updateDogsTime(id, time)
    }
    suspend fun updateDogsDate(id: Int, date: Date){
        dogsDao.updateDogsDate(id, date)
    }

    suspend fun insertDogProfile(doge: DogsInfoEntity){
        dogsDao.insertDogProfile(doge)
    }
    suspend fun updateDogProfile(doge: DogsInfoEntity) {
        dogsDao.updateDogProfile(doge)
    }
    suspend fun deleteDogProfile(id: Int) {
        dogsDao.deleteDogProfile(id)
    }
}