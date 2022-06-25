package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DogsBreedEncyclopediaDAO {
    @Query("SELECT * FROM DogsBreedEncyclopediaEntity")
    fun getAll(): LiveData<MutableList<DogsBreedEncyclopediaEntity>>
}