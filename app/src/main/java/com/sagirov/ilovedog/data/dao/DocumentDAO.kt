package com.sagirov.ilovedog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sagirov.ilovedog.domain.model.DocumentsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDAO {
    @Query("DELETE FROM DocumentsEntity WHERE id=:id")
    suspend fun deleteDocument(id: Int)

    @Insert
    suspend fun insertDocumentFile(file: DocumentsEntity)

    @Query("UPDATE DocumentsEntity SET docs=:doc WHERE id=:id")
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>)

    @Query("SELECT * FROM DocumentsEntity")
    fun getAllDocuments(): Flow<MutableList<DocumentsEntity>>

}