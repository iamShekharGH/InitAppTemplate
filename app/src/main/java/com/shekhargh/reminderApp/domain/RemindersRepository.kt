package com.shekhargh.reminderApp.domain

import com.shekhargh.reminderApp.data.db.Reminder
import kotlinx.coroutines.flow.Flow

interface RemindersRepository {

    suspend fun getItemFromId(id: Int): Reminder?
    suspend fun insertUpdateReminder(reminder: Reminder): Int
    suspend fun deleteReminder(id: Int): Int
    fun getAllTasks(): Flow<List<Reminder>>
    suspend fun updateCompletionStatus(isComplete: Boolean, id: Int)
}