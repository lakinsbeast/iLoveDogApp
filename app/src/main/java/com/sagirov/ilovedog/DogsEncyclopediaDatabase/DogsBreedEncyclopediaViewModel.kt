package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class DogsBreedEncyclopediaViewModel(private val repo: DogsBreedEncyclopediaRepository): ViewModel() {
    val allDogs: LiveData<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
    val getAllDogsProfiles: LiveData<MutableList<DogsInfoEntity>> = repo.getAllDogsProfiles()
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


    fun updateDogsTime(id: Int, time: Long) = viewModelScope.launch {
        repo.updateDogsTime(id, time)
    }
    fun updateDogsDate(id: Int, date: Date) = viewModelScope.launch {
        repo.updateDogsDate(id, date)
    }
    fun insertDogProfile(doge: DogsInfoEntity) = viewModelScope.launch {
        repo.insertDogProfile(doge)
    }
}

class DogsBreedEncyclopediaViewModelFactory(private val repo: DogsBreedEncyclopediaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogsBreedEncyclopediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DogsBreedEncyclopediaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
