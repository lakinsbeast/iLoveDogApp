package com.sagirov.ilovedog.Activities.Documents.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDAO {
    @Query("DELETE FROM DocumentsEntity WHERE id=:id")
    suspend fun deleteDocument(id: Int)
//    @Delete
//    suspend fun deleteDocument(file: DocumentsEntity)

    @Insert
    suspend fun insertDocumentFile(file: DocumentsEntity)

    @Query("UPDATE DocumentsEntity SET docs=:doc WHERE id=:id")
    suspend fun updateDocumentFile(id: Int, doc: Map<String, String>)

    @Query("SELECT * FROM DocumentsEntity")
    fun getAllDocuments(): Flow<MutableList<DocumentsEntity>>

}