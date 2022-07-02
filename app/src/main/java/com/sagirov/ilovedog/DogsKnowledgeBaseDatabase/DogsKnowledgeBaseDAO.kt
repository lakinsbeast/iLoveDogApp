package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity

@Dao
interface DogsKnowledgeBaseDAO {
    @Query("SELECT * FROM DogsKnowledgeBaseSubEntity")
    fun getSubEnt(): LiveData<MutableList<DogsKnowledgeBaseSubEntity>>

    @Query("SELECT * FROM DogsKnowledgeBaseEntity")
    fun getEnt(): LiveData<MutableList<DogsKnowledgeBaseEntity>>

    @Transaction
    @Query("SELECT * FROM DogsKnowledgeBaseEntity")
    fun getAll(): LiveData<MutableList<DogsKnowledgeBase>>
}