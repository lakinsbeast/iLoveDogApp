package com.sagirov.ilovedog.data.repoImpl

import com.sagirov.ilovedog.data.dao.DocumentDAO
import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

class DocumentRepositoryImpl(private val dao: DocumentDAO) : DocumentRepository {
    override fun getAllDocuments(): Flow<MutableList<DocumentsEntity>> {
        return dao.getAllDocuments()
    }

    override suspend fun insert(doc: DocumentsEntity) {
        dao.insertDocumentFile(doc)
    }

    override suspend fun delete(id: Int) {
        dao.deleteDocument(id)
    }

    override suspend fun update(id: Int, doc: Map<String, String>) {
        dao.updateDocumentFile(id, doc)
    }

}