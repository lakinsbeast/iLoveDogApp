package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DogsBreedEncyclopediaViewModel(private val repo: DogsBreedEncyclopediaRepository): ViewModel() {
    val allDogs: LiveData<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
    val getAllDogsProfiles: LiveData<MutableList<DogsInfoEntity>> = repo.getAllDogsProfiles()
    val getAllDocuments: LiveData<MutableList<DocumentsEntity>> = repo.getAllDocuments()

    fun insertDocumentFile(doc: DocumentsEntity) = viewModelScope.launch {
        repo.insertDocumentFile(doc)
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
