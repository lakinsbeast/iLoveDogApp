package com.sagirov.ilovedog.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
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
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>>

}