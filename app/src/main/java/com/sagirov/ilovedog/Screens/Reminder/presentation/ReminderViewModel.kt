package com.sagirov.ilovedog.Screens.Reminder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.deleteReminderUseCase
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.getAllRemindersUseCase
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.insertReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    getAllRemindersUseCase: getAllRemindersUseCase,
    val insertReminderUseCase: insertReminderUseCase,
    val deleteReminderUseCase: deleteReminderUseCase,
) : ViewModel() {
    val reminders = getAllRemindersUseCase.invoke()
    fun insert(reminder: ReminderEntity) = viewModelScope.launch {
        insertReminderUseCase.invoke(reminder)
    }

    fun delete(reminder: Map<String, String>) = viewModelScope.launch {
        deleteReminderUseCase.invoke(reminder)
    }
}