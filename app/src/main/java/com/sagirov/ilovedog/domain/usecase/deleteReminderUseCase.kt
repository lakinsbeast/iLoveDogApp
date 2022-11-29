package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.repository.ReminderRepository

class deleteReminderUseCase(
    private val repo: ReminderRepository
) {
    suspend operator fun invoke(reminder: Map<String, String>) = repo.delete(reminder)
}