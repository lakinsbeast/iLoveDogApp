package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DogsInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateBirth: Date,
    val currentTimeWalk: Long,
    val lastWalk: Date,
    val breedName: String,
    val gender: String,
    val walkingTimeConst: Long,
    val weight: Int,
    val image: String
)

@Entity
data class DocumentsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val docs: Map<String, String>
)

@Entity
data class Analyzes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val uri: Uri,
    val dateOfLaboratory: Long,
)
@Entity
data class Medications(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dependenceOnMeals: String,
    val amount: String,
    val duration: String,
    val dosage: String,
    val startTime: Long,
    val endTime: Long,
    val time: Long,
)