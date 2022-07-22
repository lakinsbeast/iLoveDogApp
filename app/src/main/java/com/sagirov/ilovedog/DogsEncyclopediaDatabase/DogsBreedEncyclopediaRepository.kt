package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import java.util.*

class DogsBreedEncyclopediaRepository(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>> = dogsDao.getAllDogsProfiles()
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>> = dogsDao.getAllDocuments()

    suspend fun insertDocumentFile(file: DocumentsEntity) {
        dogsDao.insertDocumentFile(file)
    }
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>) {
        dogsDao.updateDocumentFile(id, doc)
    }
    suspend fun deleteDocument(id: Int) {
        dogsDao.deleteDocument(id)
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
}