package com.sagirov.ilovedog.Screens.Vaccinations.presenatition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.deleteVaccinationUseCase
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.getAllVaccinationsUseCase
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.insertVaccinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    getAllVaccinationsUseCase: getAllVaccinationsUseCase,
    val insertVaccinationUseCase: insertVaccinationUseCase,
    val deleteVaccinationUseCase: deleteVaccinationUseCase
) : ViewModel() {
    val vaccination = getAllVaccinationsUseCase.invoke()
    fun insert(vac: VaccinationsEntity) = viewModelScope.launch {
        insertVaccinationUseCase.invoke(vac)
    }

    fun delete(id: Int) = viewModelScope.launch {
        deleteVaccinationUseCase.invoke(id)
    }
}