package com.sagirov.ilovedog.Activities.MainActivity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Activities.MainActivity.domain.usecase.*
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DogsInfoViewModel @Inject constructor(
    getAllProfiles: getAllProfiles,
    private val insertDogProfile: insertDogProfile,
    private val updateDogsTime: updateDogsTime,
    private val updateDogsDate: updateDogsDate,
    private val updateDogProfile: updateDogProfile,
    private val deleteDogProfile: deleteDogProfile
) : ViewModel() {
    val dogProfiles = getAllProfiles.invoke()
    fun insert(doge: DogsInfoEntity) = viewModelScope.launch {
        insertDogProfile.invoke(doge)
    }

    fun updateDogsTime(id: Int, time: Long) = viewModelScope.launch {
        updateDogsTime.invoke(id, time)
    }

    fun updateDogsDate(id: Int, date: Date) = viewModelScope.launch {
        updateDogsDate.invoke(id, date)
    }

    fun updateDogProfile(doge: DogsInfoEntity) = viewModelScope.launch {
        updateDogProfile.invoke(doge)
    }

    fun deleteDogProfile(id: Int) = viewModelScope.launch {
        deleteDogProfile.invoke(id)
    }
}