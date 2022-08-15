package com.sagirov.ilovedog.DogsEncyclopediaDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Repos.DogsInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DogsInfoViewModel @Inject constructor(private val repo: DogsInfoRepository): ViewModel() {
    val getAllDogsProfiles: LiveData<MutableList<DogsInfoEntity>> = repo.getAllDogsProfiles()

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

class DogsInfoViewModelFactory(private val repo: DogsInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogsInfoViewModel::class.java)) {
            return DogsInfoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
