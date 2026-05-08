package com.shekhargh.reminderApp.data.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.reminderApp.data.db.Priority
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.domain.RemindersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

class InsertUpdateReminderUseCaseTest {

    private val repository: RemindersRepository = mockk()
    private val useCase = InsertUpdateReminderUseCase(repository)

    @Test
    fun `invoke should return Success with id`() = runTest {
        // Given
        val reminder = Reminder(0, "Task 1", "Desc 1", false, Priority.LOW, LocalDateTime.now())
        coEvery { repository.insertUpdateReminder(reminder) } returns 1

        // When
        val result = useCase.invoke(reminder)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(1)
    }

    @Test
    fun `invoke should return Error when repository throws`() = runTest {
        // Given
        val reminder = Reminder(0, "Task 1", "Desc 1", false, Priority.LOW, LocalDateTime.now())
        val exception = RuntimeException("Error")
        coEvery { repository.insertUpdateReminder(reminder) } throws exception

        // When
        val result = useCase.invoke(reminder)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}
