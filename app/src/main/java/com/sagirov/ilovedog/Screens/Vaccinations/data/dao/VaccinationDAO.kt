package com.sagirov.ilovedog.Screens.Vaccinations.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDAO {
    @Query("SELECT * FROM VaccinationsEntity")
    fun getAllVaccinations(): Flow<MutableList<VaccinationsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVaccination(vac: VaccinationsEntity)

    @Query("DELETE FROM VaccinationsEntity WHERE id=:id")
    suspend fun deleteVaccination(id: Int)
}