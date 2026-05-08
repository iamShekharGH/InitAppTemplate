package com.shekhargh.reminderApp.data.usecase

import com.shekhargh.reminderApp.domain.RemindersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateCompletionStatusUseCaseTest {

    private val repository: RemindersRepository = mockk()
    private val useCase = UpdateCompletionStatusUseCase(repository)

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        // Given
        val params = UpdateStatusParams(isComplete = true, id = 1)
        coEvery { repository.updateCompletionStatus(true, 1) } just runs

        // When
        useCase.invoke(params)

        // Then
        coVerify { repository.updateCompletionStatus(true, 1) }
    }
}
