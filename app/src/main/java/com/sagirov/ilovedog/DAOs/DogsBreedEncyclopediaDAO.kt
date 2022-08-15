package com.sagirov.ilovedog.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity

@Dao
interface DogsBreedEncyclopediaDAO {
    @Query("SELECT * FROM DogsBreedEncyclopediaEntity")
    fun getAll(): LiveData<MutableList<DogsBreedEncyclopediaEntity>>

}