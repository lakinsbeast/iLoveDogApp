package com.sagirov.ilovedog.Repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DAOs.DocumentDAO
import com.sagirov.ilovedog.DAOs.DogsInfoDAO
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepository@Inject constructor(private val dogsDao: DocumentDAO) {
    fun getAllDocuments(): LiveData<MutableList<DocumentsEntity>> = dogsDao.getAllDocuments()

    suspend fun insertDocumentFile(file: DocumentsEntity) {
        dogsDao.insertDocumentFile(file)
    }
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>) {
        dogsDao.updateDocumentFile(id, doc)
    }
    suspend fun deleteDocument(id: Int) {
        dogsDao.deleteDocument(id)
    }
}