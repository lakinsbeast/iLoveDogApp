package com.sagirov.ilovedog.Repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DAOs.DogsInfoDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsInfoRepository @Inject constructor(private val dogsDao: DogsInfoDAO) {
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>> = dogsDao.getAllDogsProfiles()

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