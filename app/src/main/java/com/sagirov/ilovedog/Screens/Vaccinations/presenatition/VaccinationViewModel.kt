package com.sagirov.ilovedog.Screens.Vaccinations.presenatition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.deleteVaccination
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.getAllVaccinations
import com.sagirov.ilovedog.Screens.Vaccinations.domain.usecase.insertVaccination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    getAllVaccinations: getAllVaccinations,
    val insertVaccination: insertVaccination,
    val deleteVaccination: deleteVaccination
) : ViewModel() {
    val vaccination = getAllVaccinations.invoke()
    fun insert(vac: VaccinationsEntity) = viewModelScope.launch {
        insertVaccination.invoke(vac)
    }

    fun delete(id: Int) = viewModelScope.launch {
        deleteVaccination.invoke(id)
    }
}