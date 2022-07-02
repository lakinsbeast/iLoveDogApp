package com.sagirov.ilovedog.DogsKnowledgeBaseDatabase

import androidx.room.*

@Entity
data class DogsKnowledgeBaseEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val subtitle: String
)

//    (foreignKeys = [ForeignKey(entity = DogsKnowledgeBaseEntity::class, parentColumns = ["title"], childColumns = ["title2"])])
@Entity
data class DogsKnowledgeBaseSubEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title2: String,
    val text: String,
)

data class DogsKnowledgeBase (
    @Embedded val title3: DogsKnowledgeBaseEntity,
    @Relation(parentColumn = "title", entityColumn = "title2")
    val text1: List<DogsKnowledgeBaseSubEntity>
)