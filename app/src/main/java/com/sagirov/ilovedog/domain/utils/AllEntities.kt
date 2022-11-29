package com.sagirov.ilovedog.domain.utils

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

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