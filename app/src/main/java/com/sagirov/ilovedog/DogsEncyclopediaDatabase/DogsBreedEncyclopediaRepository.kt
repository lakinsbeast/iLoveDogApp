package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData

class DogsBreedEncyclopediaRepository(private val dogsDao: DogsBreedEncyclopediaDAO) {
    fun getAllDogs(): LiveData<MutableList<DogsBreedEncyclopediaEntity>> = dogsDao.getAll()
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>> = dogsDao.getAllDogsProfiles()
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>> = dogsDao.getAllDocuments()

    suspend fun insertDocumentFile(file: DocumentsEntity) {
        dogsDao.insertDocumentFile(file)
    }

    suspend fun insertDogProfile(doge: DogsInfoEntity){
        dogsDao.insertDogProfile(doge)
    }
}