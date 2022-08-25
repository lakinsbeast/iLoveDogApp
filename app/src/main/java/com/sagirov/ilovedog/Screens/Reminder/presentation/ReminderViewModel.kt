package com.sagirov.ilovedog.Screens.Reminder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.deleteReminder
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.getAllReminders
import com.sagirov.ilovedog.Screens.Reminder.domain.usecase.insertReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    getAllReminders: getAllReminders,
    val insertReminder: insertReminder,
    val deleteReminder: deleteReminder,
) : ViewModel() {
    val reminders = getAllReminders.invoke()
    fun insert(reminder: ReminderEntity) = viewModelScope.launch {
        insertReminder.invoke(reminder)
    }

    fun delete(reminder: Map<String, String>) = viewModelScope.launch {
        deleteReminder.invoke(reminder)
    }
}