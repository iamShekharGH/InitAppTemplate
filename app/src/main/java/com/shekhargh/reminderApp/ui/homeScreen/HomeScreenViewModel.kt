package com.shekhargh.reminderApp.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.data.usecase.GetAllTasksUseCase
import com.shekhargh.reminderApp.data.usecase.InsertUpdateReminderUseCase
import com.shekhargh.reminderApp.data.usecase.Result
import com.shekhargh.reminderApp.util.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val insertUpdateReminderUseCase: InsertUpdateReminderUseCase
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val homeScreenState = _homeScreenState.asStateFlow()

    init {
        getAllReminders()
    }

    fun getAllReminders() {
        viewModelScope.launch {
            when (val result = getAllTasksUseCase()) {
                Result.Empty -> {
                    _homeScreenState.value = HomeScreenUiState.Items(emptyList())
                }

                is Result.Error -> {
                    _homeScreenState.value = HomeScreenUiState.Error

                }

                is Result.Success<Flow<List<Reminder>>> -> {
                    result.data.collect {
                        _homeScreenState.value = HomeScreenUiState.Items(it)
                    }

                }
            }
        }
    }

    fun populateSampleData() {
        viewModelScope.launch {
            SampleData.reminders.forEach { sampleReminder ->
                // Use id = 0 to let Room auto-generate IDs for these as new entries
                insertUpdateReminderUseCase(sampleReminder.copy(id = 0))
            }
        }
    }

    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            insertUpdateReminderUseCase(reminder)
        }
    }

    fun updateCompletionStatus(reminder: Reminder, isCompleted: Boolean) {
        viewModelScope.launch {
            insertUpdateReminderUseCase(reminder.copy(completionStatus = isCompleted))
        }
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data object Error : HomeScreenUiState
    data object Initial : HomeScreenUiState
    data class Items(val items: List<Reminder>) : HomeScreenUiState
}
