package com.sagirov.ilovedog.Screens.Reminder.domain.usecase

import com.sagirov.ilovedog.Screens.Reminder.domain.repository.ReminderRepository

class deleteReminderUseCase(
    private val repo: ReminderRepository
) {
    suspend operator fun invoke(reminder: Map<String, String>) = repo.delete(reminder)
}