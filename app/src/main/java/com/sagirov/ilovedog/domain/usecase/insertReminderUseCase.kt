package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.ReminderEntity
import com.sagirov.ilovedog.domain.repository.ReminderRepository

class insertReminderUseCase(
    private val repo: ReminderRepository
) {
    suspend operator fun invoke(reminder: ReminderEntity) = repo.insert(reminder)
}