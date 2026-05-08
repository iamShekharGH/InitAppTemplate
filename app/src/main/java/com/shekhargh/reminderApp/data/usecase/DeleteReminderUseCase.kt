package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.domain.RemindersRepository
import com.shekhargh.reminderApp.domain.UseCase
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val repository: RemindersRepository
) : UseCase<Int, Result<Int>> {
    override suspend fun invoke(input: Int): Result<Int> {
        return asResult { repository.deleteReminder(input) }
    }
}