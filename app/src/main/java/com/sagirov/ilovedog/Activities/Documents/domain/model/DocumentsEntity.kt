package com.sagirov.ilovedog.Activities.Documents.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DocumentsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val docs: Map<String, String>
)