package com.sagirov.ilovedog.domain.repository

import com.sagirov.ilovedog.domain.model.DocumentsEntity
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    fun getAllDocuments(): Flow<MutableList<DocumentsEntity>>
    suspend fun insert(doc: DocumentsEntity)
    suspend fun delete(id: Int)
    suspend fun update(id: Int, doc: Map<String, String>)
}