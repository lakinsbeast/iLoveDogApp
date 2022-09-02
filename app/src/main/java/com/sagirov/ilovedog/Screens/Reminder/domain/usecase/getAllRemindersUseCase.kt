package com.sagirov.ilovedog.Screens.Reminder.domain.usecase

import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class getAllRemindersUseCase(
    private val repo: ReminderRepository
) {
    operator fun invoke(): Flow<List<ReminderEntity>> = repo.getAllReminders()
}