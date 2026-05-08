package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.domain.RemindersRepository
import com.shekhargh.reminderApp.domain.UseCase
import javax.inject.Inject

class InsertUpdateReminderUseCase @Inject constructor(private val repository: RemindersRepository) :
    UseCase<Reminder, Result<Int>> {
    override suspend fun invoke(input: Reminder): Result<Int> {
        return asResult { repository.insertUpdateReminder(input) }
    }
}