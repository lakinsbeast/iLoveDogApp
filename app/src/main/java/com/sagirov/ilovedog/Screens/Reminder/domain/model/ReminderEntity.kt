package com.sagirov.ilovedog.Screens.Reminder.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reminder: Map<String, String>
)