package com.sagirov.ilovedog.Screens.Reminder.domain.usecase

import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository

class insertReminder(
    private val repo: ReminderRepository
) {
    suspend operator fun invoke(reminder: ReminderEntity) = repo.insert(reminder)
}