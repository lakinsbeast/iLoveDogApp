package com.sagirov.ilovedog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.domain.usecase.deleteDocumentUseCase
import com.sagirov.ilovedog.domain.usecase.getAllDocumentsUseCase
import com.sagirov.ilovedog.domain.usecase.insertDocumentUseCase
import com.sagirov.ilovedog.domain.usecase.updateDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    getAllDocumentsUseCase: getAllDocumentsUseCase,
    private val insertDocumentUseCase: insertDocumentUseCase,
    private val updateDocumentUseCase: updateDocumentUseCase,
    private val deleteDocumentUseCase: deleteDocumentUseCase,
) : ViewModel() {
    val documents = getAllDocumentsUseCase.invoke()
    fun insert(doc: DocumentsEntity) = viewModelScope.launch {
        insertDocumentUseCase.invoke(doc)
    }

    fun update(id: Int, doc: Map<String, String>) = viewModelScope.launch {
        updateDocumentUseCase.invoke(id, doc)
    }

    fun delete(id: Int) = viewModelScope.launch {
        deleteDocumentUseCase.invoke(id)
    }
}