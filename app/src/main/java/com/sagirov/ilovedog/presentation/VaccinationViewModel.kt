package com.sagirov.ilovedog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.domain.usecase.deleteVaccinationUseCase
import com.sagirov.ilovedog.domain.usecase.getAllVaccinationsUseCase
import com.sagirov.ilovedog.domain.usecase.insertVaccinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    getAllVaccinationsUseCase: getAllVaccinationsUseCase,
    private val insertVaccinationUseCase: insertVaccinationUseCase,
    private val deleteVaccinationUseCase: deleteVaccinationUseCase
) : ViewModel() {
    val vaccination = getAllVaccinationsUseCase.invoke()
    fun insert(vac: VaccinationsEntity) = viewModelScope.launch {
        insertVaccinationUseCase.invoke(vac)
    }

    fun delete(id: Int) = viewModelScope.launch {
        deleteVaccinationUseCase.invoke(id)
    }
}