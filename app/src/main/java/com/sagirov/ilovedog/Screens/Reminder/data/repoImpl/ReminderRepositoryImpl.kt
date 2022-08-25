package com.sagirov.ilovedog.Screens.Reminder.data.repoImpl

import com.sagirov.ilovedog.Screens.Reminder.data.dao.ReminderDAO
import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(private val dao: ReminderDAO) : ReminderRepository {
    override fun getAllReminders(): Flow<List<ReminderEntity>> {
        return dao.getAllReminders()
    }

    override suspend fun insert(reminder: ReminderEntity) {
        dao.insertReminder(reminder)
    }

    override suspend fun delete(reminder: Map<String, String>) {
        dao.deleteReminder(reminder)
    }


}