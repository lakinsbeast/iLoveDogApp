package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DogsBreedEncyclopediaViewModel @Inject constructor(private val repo: DogsBreedEncyclopediaRepository): ViewModel() {
    val allDogs: LiveData<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
    val getAllDogsProfiles: LiveData<MutableList<DogsInfoEntity>> = repo.getAllDogsProfiles()
    val getAllDocuments: LiveData<MutableList<DocumentsEntity>> = repo.getAllDocuments()
    val getAllVaccinations: LiveData<MutableList<VaccinationsEntity>> = repo.getAllVaccinations()

    fun insertDocumentFile(doc: DocumentsEntity) = viewModelScope.launch {
        repo.insertDocumentFile(doc)
    }
    fun updateDocumentFile(id: Int, doc: Map<String, String>) = viewModelScope.launch {
        repo.updateDocumentFile(id, doc)
    }
    fun deleteDocumentFile(id: Int) = viewModelScope.launch {
        repo.deleteDocument(id)
    }

    fun insertVaccination(vac: VaccinationsEntity) = viewModelScope.launch {
        repo.insertVaccination(vac)
    }
    fun deleteVaccination(id: Int) = viewModelScope.launch {
        repo.deleteVaccination(id)
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
    fun updateDogProfile(doge: DogsInfoEntity) = viewModelScope.launch {
        repo.updateDogProfile(doge)
    }
    fun deleteDogProfile(id: Int) = viewModelScope.launch {
        repo.deleteDogProfile(id)
    }
}

class DogsBreedEncyclopediaViewModelFactory(private val repo: DogsBreedEncyclopediaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogsBreedEncyclopediaViewModel::class.java)) {
            return DogsBreedEncyclopediaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
