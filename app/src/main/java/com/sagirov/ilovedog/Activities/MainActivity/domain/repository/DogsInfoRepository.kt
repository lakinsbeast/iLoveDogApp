package com.sagirov.ilovedog.Activities.MainActivity.domain.repository

import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DogsInfoRepository {
    fun getAllProfiles(): Flow<MutableList<DogsInfoEntity>>
    suspend fun insert(doge: DogsInfoEntity)
    suspend fun updateDogsTime(id: Int, time: Long)
    suspend fun updateDogsDate(id: Int, date: Date)
    suspend fun updateDogProfile(doge: DogsInfoEntity)
    suspend fun delete(id: Int)
}