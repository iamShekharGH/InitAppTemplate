package com.shekhargh.reminderApp.data.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.shekhargh.reminderApp.data.db.Priority
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.domain.RemindersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

class GetAllTasksUseCaseTest {

    private val repository: RemindersRepository = mockk()
    private val useCase = GetAllTasksUseCase(repository)

    @Test
    fun `invoke should return Success with flow of reminders`() = runTest {
        // Given
        val reminders = listOf(
            Reminder(1, "Task 1", "Desc 1", false, Priority.LOW, LocalDateTime.now()),
            Reminder(2, "Task 2", "Desc 2", true, Priority.HIGH, LocalDateTime.now())
        )
        coEvery { repository.getAllTasks() } returns flowOf(reminders)

        // When
        val result = useCase.invoke()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        (result as Result.Success).data.test {
            assertThat(awaitItem()).isEqualTo(reminders)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should return Error when repository throws`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        coEvery { repository.getAllTasks() } throws exception

        // When
        val result = useCase.invoke()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}
