package com.sagirov.ilovedog.DogsEncyclopediaDatabase

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