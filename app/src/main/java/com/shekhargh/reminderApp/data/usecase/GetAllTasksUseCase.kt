package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.domain.NoParameterUseCase
import com.shekhargh.reminderApp.domain.RemindersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val repository: RemindersRepository) :
    NoParameterUseCase<Result<Flow<List<Reminder>>>> {
    override suspend fun invoke(): Result<Flow<List<Reminder>>> {
        return asResult { repository.getAllTasks() }
    }
}