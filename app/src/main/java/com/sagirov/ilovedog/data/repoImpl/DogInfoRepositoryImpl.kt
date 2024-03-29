package com.sagirov.ilovedog.data.repoImpl

import com.sagirov.ilovedog.data.dao.DogsInfoDAO
import com.sagirov.ilovedog.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.repository.DogsInfoRepository
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