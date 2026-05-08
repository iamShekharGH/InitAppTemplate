package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.domain.RemindersRepository
import com.shekhargh.reminderApp.domain.UseCase
import javax.inject.Inject

class GetItemFromIdUseCase @Inject constructor(
    private val repository: RemindersRepository
) : UseCase<Int, Result<Reminder>> {

    override suspend fun invoke(input: Int): Result<Reminder> {
        return asResult { repository.getItemFromId(input) }
    }
}
