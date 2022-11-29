package com.sagirov.ilovedog.domain.repository

import com.sagirov.ilovedog.domain.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<ReminderEntity>>
    suspend fun insert(reminder: ReminderEntity)
    suspend fun delete(reminder: Map<String, String>)
}