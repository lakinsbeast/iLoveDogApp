package com.sagirov.ilovedog.Screens.Reminder.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDAO {
    @Query("SELECT * FROM ReminderEntity")
    fun getAllReminders(): Flow<MutableList<ReminderEntity>>

    @Insert
    suspend fun insertReminder(reminder: ReminderEntity)

    @Query("DELETE FROM ReminderEntity WHERE reminder=:reminder")
    suspend fun deleteReminder(reminder: Map<String, String>)
}