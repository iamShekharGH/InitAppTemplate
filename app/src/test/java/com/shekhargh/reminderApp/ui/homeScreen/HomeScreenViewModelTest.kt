package com.shekhargh.reminderApp.ui.homeScreen

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.shekhargh.reminderApp.data.usecase.GetAllTasksUseCase
import com.shekhargh.reminderApp.data.usecase.InsertUpdateReminderUseCase
import com.shekhargh.reminderApp.data.usecase.Result
import com.shekhargh.reminderApp.util.MainDispatcherRule
import com.shekhargh.reminderApp.util.SampleData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllTasksUseCase: GetAllTasksUseCase = mockk()
    private val insertUpdateReminderUseCase: InsertUpdateReminderUseCase = mockk()

    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setup() {
        // Default behavior for init block
        coEvery { getAllTasksUseCase() } returns Result.Empty
    }

    @Test
    fun `init should call getAllReminders and update state to Loading initially`() = runTest {
        // When
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // Then
        viewModel.homeScreenState.test {
            // Depending on how fast collect works, we might see Loading or the result of getAllTasksUseCase
            val firstItem = awaitItem()
            if (firstItem is HomeScreenUiState.Loading) {
                assertThat(awaitItem()).isEqualTo(HomeScreenUiState.Items(emptyList()))
            } else {
                assertThat(firstItem).isEqualTo(HomeScreenUiState.Items(emptyList()))
            }
        }
    }

    @Test
    fun `getAllReminders should update state to Items when Success`() = runTest {
        // Given
        val reminders = SampleData.reminders
        coEvery { getAllTasksUseCase() } returns Result.Success(flowOf(reminders))

        // When
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // Then
        viewModel.homeScreenState.test {
            assertThat(awaitItem()).isInstanceOf(HomeScreenUiState.Loading::class.java)
            assertThat(awaitItem()).isEqualTo(HomeScreenUiState.Items(reminders))
        }
    }

    @Test
    fun `getAllReminders should update state to Error when Result Error`() = runTest {
        // Given
        coEvery { getAllTasksUseCase() } returns Result.Error(Exception("Test Error"))

        // When
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // Then
        viewModel.homeScreenState.test {
            assertThat(awaitItem()).isInstanceOf(HomeScreenUiState.Loading::class.java)
            assertThat(awaitItem()).isEqualTo(HomeScreenUiState.Error)
        }
    }

    @Test
    fun `populateSampleData should call insertUpdateReminderUseCase for each sample reminder`() = runTest {
        // Given
        coEvery { insertUpdateReminderUseCase(any()) } returns Result.Success(1)
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // When
        viewModel.populateSampleData()

        // Then
        coVerify(exactly = SampleData.reminders.size) {
            insertUpdateReminderUseCase(match { it.id == 0 })
        }
    }

    @Test
    fun `saveReminder should call insertUpdateReminderUseCase`() = runTest {
        // Given
        val reminder = SampleData.reminders[0]
        coEvery { insertUpdateReminderUseCase(reminder) } returns Result.Success(1)
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // When
        viewModel.saveReminder(reminder)

        // Then
        coVerify { insertUpdateReminderUseCase(reminder) }
    }

    @Test
    fun `updateCompletionStatus should call insertUpdateReminderUseCase with updated status`() = runTest {
        // Given
        val reminder = SampleData.reminders[0].copy(completionStatus = false)
        val expectedReminder = reminder.copy(completionStatus = true)
        coEvery { insertUpdateReminderUseCase(any()) } returns Result.Success(1)
        viewModel = HomeScreenViewModel(getAllTasksUseCase, insertUpdateReminderUseCase)

        // When
        viewModel.updateCompletionStatus(reminder, isCompleted = true)

        // Then
        coVerify { insertUpdateReminderUseCase(expectedReminder) }
    }
}
