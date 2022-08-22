package com.sagirov.ilovedog.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.repos.VaccinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(private val repo: VaccinationRepository): ViewModel() {
    val getAllVaccinations: LiveData<MutableList<VaccinationsEntity>> = repo.getAllVaccinations()

    fun insertVaccination(vac: VaccinationsEntity) = viewModelScope.launch {
        repo.insertVaccination(vac)
    }
    fun deleteVaccination(id: Int) = viewModelScope.launch {
        repo.deleteVaccination(id)
    }
}
//class VaccinationViewModelFactory(private val repo: VaccinationRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(VaccinationViewModel::class.java)) {
//            return VaccinationViewModel(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}