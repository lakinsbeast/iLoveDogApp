package com.sagirov.ilovedog.Activities.MainActivity.repo

import com.sagirov.ilovedog.Activities.MainActivity.dao.DogsInfoDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsInfoRepository @Inject constructor(private val dogsDao: DogsInfoDAO) {
    fun getAllDogsProfiles(): Flow<MutableList<DogsInfoEntity>> = dogsDao.getAllDogsProfiles()

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