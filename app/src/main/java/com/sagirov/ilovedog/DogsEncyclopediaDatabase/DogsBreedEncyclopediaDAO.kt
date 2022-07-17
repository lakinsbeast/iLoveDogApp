package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogsBreedEncyclopediaDAO {
    @Query("SELECT * FROM DogsBreedEncyclopediaEntity")
    fun getAll(): LiveData<MutableList<DogsBreedEncyclopediaEntity>>

    @Insert
    suspend fun insertDogProfile(doge: DogsInfoEntity)

    @Insert
    suspend fun insertDocumentFile(file: DocumentsEntity)

    @Query("SELECT * FROM DocumentsEntity")
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>>

    @Query("SELECT * FROM DogsInfoEntity")
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>>
}