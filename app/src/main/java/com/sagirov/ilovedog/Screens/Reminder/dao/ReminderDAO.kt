package com.sagirov.ilovedog.Screens.Reminder.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.ReminderEntity

@Dao
interface ReminderDAO {
    @Query("SELECT * FROM ReminderEntity")
    fun getAllReminders(): LiveData<MutableList<ReminderEntity>>

    @Insert
    suspend fun insertReminder(reminder: ReminderEntity)

    @Query("DELETE FROM ReminderEntity WHERE reminder=:reminder")
    suspend fun deleteReminder(reminder: Map<String, String>)
}