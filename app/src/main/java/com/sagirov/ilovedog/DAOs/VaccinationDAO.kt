package com.sagirov.ilovedog.DAOs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity

@Dao
interface VaccinationDAO {
    @Query("SELECT * FROM VaccinationsEntity")
    fun getAllVaccinations(): LiveData<MutableList<VaccinationsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVaccination(vac: VaccinationsEntity)

    @Query("DELETE FROM VaccinationsEntity WHERE id=:id")
    suspend fun deleteVaccination(id: Int)
}