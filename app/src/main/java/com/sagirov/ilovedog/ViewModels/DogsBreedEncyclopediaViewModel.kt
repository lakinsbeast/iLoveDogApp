package com.sagirov.ilovedog.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.Repos.DocumentRepository
import com.sagirov.ilovedog.Repos.DogsBreedEncyclopediaRepository
import com.sagirov.ilovedog.Repos.DogsInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogsBreedEncyclopediaViewModel @Inject constructor(private val repo: DogsBreedEncyclopediaRepository): ViewModel() {
    val allDogs: LiveData<MutableList<DogsBreedEncyclopediaEntity>> = repo.getAllDogs()

}
class DogsBreedEncyclopediaViewModelFactory(private val repo: DogsBreedEncyclopediaRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogsBreedEncyclopediaViewModel::class.java)) {
            return DogsBreedEncyclopediaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}