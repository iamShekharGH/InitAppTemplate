package com.shekhargh.reminderApp.data.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.reminderApp.domain.RemindersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteReminderUseCaseTest {

    private val repository: RemindersRepository = mockk()
    private val useCase = DeleteReminderUseCase(repository)

    @Test
    fun `invoke should return Success with deleted rows count`() = runTest {
        // Given
        coEvery { repository.deleteReminder(1) } returns 1

        // When
        val result = useCase.invoke(1)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(1)
    }

    @Test
    fun `invoke should return Error when repository throws`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { repository.deleteReminder(1) } throws exception

        // When
        val result = useCase.invoke(1)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}
