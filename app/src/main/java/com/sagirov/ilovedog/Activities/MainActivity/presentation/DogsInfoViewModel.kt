package com.sagirov.ilovedog.Activities.MainActivity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Activities.MainActivity.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.Activities.MainActivity.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DogsInfoViewModel @Inject constructor(
    getAllProfilesUseCase: getAllProfilesUseCase,
    private val insertDogProfileUseCase: insertDogProfileUseCase,
    private val updateDogsTimeUseCase: updateDogsTimeUseCase,
    private val updateDogsDateUseCase: updateDogsDateUseCase,
    private val updateDogProfileUseCase: updateDogProfileUseCase,
    private val deleteDogProfileUseCase: deleteDogProfileUseCase
) : ViewModel() {
    val dogProfiles = getAllProfilesUseCase.invoke()
    fun insert(doge: DogsInfoEntity) = viewModelScope.launch {
        insertDogProfileUseCase(doge)
    }

    fun updateDogsTime(id: Int, time: Long) = viewModelScope.launch {
        updateDogsTimeUseCase(id, time)
    }

    fun updateDogsDate(id: Int, date: Date) = viewModelScope.launch {
        updateDogsDateUseCase(id, date)
    }

    fun updateDogProfile(doge: DogsInfoEntity) = viewModelScope.launch {
        updateDogProfileUseCase(doge)
    }

    fun deleteDogProfile(id: Int) = viewModelScope.launch {
        deleteDogProfileUseCase(id)
    }
}