package com.sagirov.ilovedog.Screens.Vaccinations.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VaccinationsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val drugName: String,
    val dateOfVaccinations: Long,
//    val expirationDateOfVaccinations: Long,
//    val veterinarianName: String,
//    val vetClinicName: String,
)