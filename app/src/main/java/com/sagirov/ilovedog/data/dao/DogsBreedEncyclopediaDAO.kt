package com.sagirov.ilovedog.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DogsBreedEncyclopediaDAO {
    @Query("SELECT * FROM DogsBreedEncyclopediaEntity")
    fun getAll(): Flow<MutableList<DogsBreedEncyclopediaEntity>>

    @Query("SELECT * FROM DogsBreedEncyclopediaEntity WHERE id=:id")
    suspend fun getDogById(id: Int): DogsBreedEncyclopediaEntity
}