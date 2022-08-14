package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Update
    suspend fun updateDogProfile(doge: DogsInfoEntity)

    @Query("DELETE FROM DogsInfoEntity WHERE id=:id")
    suspend fun deleteDogProfile(id: Int)

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

    @Query("SELECT * FROM VaccinationsEntity")
    fun getAllVaccinations(): LiveData<MutableList<VaccinationsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVaccination(vac: VaccinationsEntity)

    @Query("DELETE FROM VaccinationsEntity WHERE id=:id")
    suspend fun deleteVaccination(id: Int)

}