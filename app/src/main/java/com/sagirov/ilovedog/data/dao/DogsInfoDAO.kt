package com.sagirov.ilovedog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sagirov.ilovedog.domain.model.DogsInfoEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DogsInfoDAO {
    @Insert
    suspend fun insertDogProfile(doge: DogsInfoEntity)

    @Query("UPDATE DogsInfoEntity SET currentTimeWalk=:time WHERE id=:id")
    suspend fun updateDogsTime(id: Int, time: Long)

    @Query("UPDATE DogsInfoEntity SET lastWalk=:date WHERE id=:id")
    suspend fun updateDogsDate(id: Int, date: Date)

    @Update
    suspend fun updateDogProfile(doge: DogsInfoEntity)

    @Query("DELETE FROM DogsInfoEntity WHERE id=:id")
    suspend fun deleteDogProfile(id: Int)

    @Query("SELECT * FROM DogsInfoEntity")
    fun getAllDogsProfiles(): Flow<MutableList<DogsInfoEntity>>

}