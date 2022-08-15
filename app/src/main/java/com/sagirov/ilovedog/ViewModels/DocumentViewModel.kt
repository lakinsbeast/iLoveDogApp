package com.sagirov.ilovedog.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoViewModel
import com.sagirov.ilovedog.Repos.DocumentRepository
import com.sagirov.ilovedog.Repos.DogsInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(private val repo: DocumentRepository): ViewModel() {
    val getAllDocuments: LiveData<MutableList<DocumentsEntity>> = repo.getAllDocuments()

    fun insertDocumentFile(doc: DocumentsEntity) = viewModelScope.launch {
        repo.insertDocumentFile(doc)
    }
    fun updateDocumentFile(id: Int, doc: Map<String, String>) = viewModelScope.launch {
        repo.updateDocumentFile(id, doc)
    }
    fun deleteDocumentFile(id: Int) = viewModelScope.launch {
        repo.deleteDocument(id)
    }
}
class DocumentViewModelFactory(private val repo: DocumentRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            return DocumentViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}