package com.sagirov.ilovedog.domain.usecase

import com.sagirov.ilovedog.domain.model.ReminderEntity
import com.sagirov.ilovedog.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class getAllRemindersUseCase(
    private val repo: ReminderRepository
) {
    operator fun invoke(): Flow<List<ReminderEntity>> = repo.getAllReminders()
}