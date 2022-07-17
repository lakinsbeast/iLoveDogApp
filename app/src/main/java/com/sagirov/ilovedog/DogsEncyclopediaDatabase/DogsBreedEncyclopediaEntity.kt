package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

