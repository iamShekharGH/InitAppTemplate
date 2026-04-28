package com.shekhargh.reminderApp.data.repo

import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.data.db.RemindersDao
import com.shekhargh.reminderApp.domain.RemindersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemindersRepositoryImpl @Inject constructor(
    private val remindersDao: RemindersDao
) : RemindersRepository {

    override suspend fun getItemFromId(id: Int): Reminder? = remindersDao.getItemFromId(id)

    override suspend fun insertUpdateReminder(reminder: Reminder): Int = remindersDao.upsertReminder(reminder).toInt()

    override suspend fun deleteReminder(id: Int): Int = remindersDao.deleteReminder(id)

    override fun getAllTasks(): Flow<List<Reminder>> = remindersDao.getAllReminders()

    override suspend fun updateCompletionStatus(isComplete: Boolean, id: Int) = remindersDao.updateCompletionStatus(isComplete = isComplete, id = id)


}