package com.sagirov.ilovedog.Screens.Reminder.repos

import androidx.lifecycle.LiveData
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.dao.ReminderDAO
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReminderRepository @Inject constructor(private val reminderDao: ReminderDAO) {
    fun getAllReminders(): LiveData<MutableList<ReminderEntity>> = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: ReminderEntity) {
        reminderDao.insertReminder(reminder)
    }
    suspend fun deleteReminder(reminder: Map<String, String>) {
        reminderDao.deleteReminder(reminder)
    }
}