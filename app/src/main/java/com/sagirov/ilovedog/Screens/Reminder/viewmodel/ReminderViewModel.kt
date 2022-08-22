package com.sagirov.ilovedog.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.repos.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val repo: ReminderRepository): ViewModel() {
    val getAllReminders: LiveData<MutableList<ReminderEntity>> = repo.getAllReminders()

    fun insertReminder(reminder: ReminderEntity) = viewModelScope.launch {
        repo.insertReminder(reminder)
    }
    fun deleteReminder(reminder: Map<String, String>) = viewModelScope.launch {
        repo.deleteReminder(reminder)
    }

}

//class ReminderViewModelFactory(private val repo: ReminderRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
//            return ReminderViewModel(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}