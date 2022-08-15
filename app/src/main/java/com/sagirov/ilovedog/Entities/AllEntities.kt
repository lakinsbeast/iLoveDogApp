package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DogsBreedEncyclopediaEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "breed_name") val breedName: String,
    val origin: String,
    @ColumnInfo(name = "breed_group")val breedGroup: String,
    @ColumnInfo(name = "life_span")val lifeSpan: String,
    val type: String,
    val temperament: String,
    @ColumnInfo(name = "male_height")val male_height: String,
    @ColumnInfo(name = "female_height")val female_height: String,
    @ColumnInfo(name = "male_weight")val male_weight: String,
    @ColumnInfo(name = "female_weight")val female_weight: String,
    val colors: String,
    @ColumnInfo(name = "litter_size")val litterSize: String,
    val description: String,
    @ColumnInfo(name = "image_file") val imageFile: String
)

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
data class VaccinationsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val drugName: String,
    val dateOfVaccinations: Long,
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