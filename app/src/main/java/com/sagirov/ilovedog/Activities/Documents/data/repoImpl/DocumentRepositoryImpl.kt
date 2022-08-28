package com.sagirov.ilovedog.Activities.Documents.data.repoImpl

import com.sagirov.ilovedog.Activities.Documents.data.dao.DocumentDAO
import com.sagirov.ilovedog.Activities.Documents.domain.repository.DocumentRepository
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
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