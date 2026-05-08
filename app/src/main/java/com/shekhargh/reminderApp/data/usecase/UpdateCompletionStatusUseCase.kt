package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.domain.NoOutputUseCase
import com.shekhargh.reminderApp.domain.RemindersRepository
import javax.inject.Inject

class UpdateCompletionStatusUseCase @Inject constructor(
    private val repository: RemindersRepository
) : NoOutputUseCase<UpdateStatusParams> {
    override suspend fun invoke(input: UpdateStatusParams) {
        asResult { repository.updateCompletionStatus(input.isComplete, input.id) }
    }
}

data class UpdateStatusParams(
    val isComplete: Boolean,
    val id: Int
)