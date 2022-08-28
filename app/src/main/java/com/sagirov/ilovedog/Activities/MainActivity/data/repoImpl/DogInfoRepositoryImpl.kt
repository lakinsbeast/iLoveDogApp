package com.sagirov.ilovedog.Activities.MainActivity.data.repoImpl

import com.sagirov.ilovedog.Activities.MainActivity.data.dao.DogsInfoDAO
import com.sagirov.ilovedog.Activities.MainActivity.domain.repository.DogsInfoRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

class DogInfoRepositoryImpl(private val dao: DogsInfoDAO) : DogsInfoRepository {
    override fun getAllProfiles(): Flow<MutableList<DogsInfoEntity>> {
        return dao.getAllDogsProfiles()
    }

    override suspend fun insert(doge: DogsInfoEntity) {
        dao.insertDogProfile(doge)
    }

    override suspend fun updateDogsTime(id: Int, time: Long) {
        dao.updateDogsTime(id, time)
    }

    override suspend fun updateDogsDate(id: Int, date: Date) {
        dao.updateDogsDate(id, date)
    }

    override suspend fun updateDogProfile(doge: DogsInfoEntity) {
        dao.updateDogProfile(doge)
    }

    override suspend fun delete(id: Int) {
        dao.deleteDogProfile(id)
    }
}