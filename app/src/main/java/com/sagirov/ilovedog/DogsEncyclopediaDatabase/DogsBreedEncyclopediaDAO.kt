package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface DogsBreedEncyclopediaDAO {
    @Query("SELECT * FROM DogsBreedEncyclopediaEntity")
    fun getAll(): LiveData<MutableList<DogsBreedEncyclopediaEntity>>

    @Insert
    suspend fun insertDogProfile(doge: DogsInfoEntity)

    @Query("UPDATE DogsInfoEntity SET currentTimeWalk=:time WHERE id=:id")
    suspend fun updateDogsTime(id: Int, time: Long)

    @Query("UPDATE DogsInfoEntity SET lastWalk=:date WHERE id=:id")
    suspend fun updateDogsDate(id: Int, date: Date)

    @Query("SELECT * FROM DogsInfoEntity")
    fun getAllDogsProfiles(): LiveData<MutableList<DogsInfoEntity>>

    @Query("DELETE FROM DocumentsEntity WHERE id=:id")
    suspend fun deleteDocument(id: Int)
//    @Delete
//    suspend fun deleteDocument(file: DocumentsEntity)

    @Insert
    suspend fun insertDocumentFile(file: DocumentsEntity)

    @Query("UPDATE DocumentsEntity SET docs=:doc WHERE id=:id")
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>)

    @Query("SELECT * FROM DocumentsEntity")
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>>


}