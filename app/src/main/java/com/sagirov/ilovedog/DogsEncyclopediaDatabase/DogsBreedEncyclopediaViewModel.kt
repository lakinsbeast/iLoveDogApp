package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DogsBreedEncyclopediaViewModel(private val repo: DogsBreedEncyclopediaRepository): ViewModel() {
    val allDogs: LiveData<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()
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
