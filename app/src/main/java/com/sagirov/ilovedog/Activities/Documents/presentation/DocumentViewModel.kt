package com.sagirov.ilovedog.Activities.Documents.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Activities.Documents.domain.usecase.deleteDocument
import com.sagirov.ilovedog.Activities.Documents.domain.usecase.getAllDocuments
import com.sagirov.ilovedog.Activities.Documents.domain.usecase.insertDocument
import com.sagirov.ilovedog.Activities.Documents.domain.usecase.updateDocument
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    getAllDocuments: getAllDocuments,
    private val insertDocument: insertDocument,
    private val updateDocument: updateDocument,
    private val deleteDocument: deleteDocument,
) : ViewModel() {
    val documents = getAllDocuments.invoke()
    fun insert(doc: DocumentsEntity) = viewModelScope.launch {
        insertDocument.invoke(doc)
    }

    fun update(id: Int, doc: Map<String, String>) = viewModelScope.launch {
        updateDocument.invoke(id, doc)
    }

    fun delete(id: Int) = viewModelScope.launch {
        deleteDocument.invoke(id)
    }
}