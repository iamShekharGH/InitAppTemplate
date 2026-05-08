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

class GetItemFromIdUseCaseTest {

    private val repository: RemindersRepository = mockk()
    private val useCase = GetItemFromIdUseCase(repository)

    @Test
    fun `invoke should return Success when item exists`() = runTest {
        // Given
        val reminder = Reminder(1, "Task 1", "Desc 1", false, Priority.LOW, LocalDateTime.now())
        coEvery { repository.getItemFromId(1) } returns reminder

        // When
        val result = useCase.invoke(1)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(reminder)
    }

    @Test
    fun `invoke should return Empty when item does not exist`() = runTest {
        // Given
        coEvery { repository.getItemFromId(1) } returns null

        // When
        val result = useCase.invoke(1)

        // Then
        assertThat(result).isInstanceOf(Result.Empty::class.java)
    }

    @Test
    fun `invoke should return Error when repository throws`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { repository.getItemFromId(1) } throws exception

        // When
        val result = useCase.invoke(1)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(exception)
    }
}
