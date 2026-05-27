package com.shekhargh.reminderApp.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.data.usecase.CheckNotificationPermissionUseCase
import com.shekhargh.reminderApp.data.usecase.GetAllTasksUseCase
import com.shekhargh.reminderApp.data.usecase.InsertUpdateReminderUseCase
import com.shekhargh.reminderApp.data.usecase.Result
import com.shekhargh.reminderApp.util.SampleData
import com.shekhargh.reminderApp.workManagerFiles.scheduler.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val insertUpdateReminderUseCase: InsertUpdateReminderUseCase,
    private val checkNotificationPermissionUseCase: CheckNotificationPermissionUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _showNotificationPermissionRequest = MutableStateFlow(false)
    val showNotificationPermissionRequest = _showNotificationPermissionRequest.asStateFlow()

    init {
        getAllReminders()
        checkNotificationPermission()
    }

    fun checkNotificationPermission() {
        if (!checkNotificationPermissionUseCase()) {
            _showNotificationPermissionRequest.value = true
        }
    }

    fun onPermissionRequestHandled() {
        _showNotificationPermissionRequest.value = false
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
            val result = insertUpdateReminderUseCase(reminder)
            if (result is Result.Success) {
                // For new reminders (id=0), use the generated ID from the database.
                // For updates, use the existing reminder.id, as Room's @Upsert may return -1 for updates.
                val finalId = if (reminder.id == 0) result.data else reminder.id
                reminderScheduler.scheduleReminder(reminder.copy(id = finalId))
            }
        }
    }

    fun updateCompletionStatus(reminder: Reminder, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedReminder = reminder.copy(completionStatus = isCompleted)
            val result = insertUpdateReminderUseCase(updatedReminder)
            if (result is Result.Success) {
                if (isCompleted) {
                    reminderScheduler.cancelReminder(reminder.id)
                } else {
                    // Use the existing ID for scheduling updates
                    reminderScheduler.scheduleReminder(updatedReminder)
                }
            }
        }
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data object Error : HomeScreenUiState
    data object Initial : HomeScreenUiState
    data class Items(val items: List<Reminder>) : HomeScreenUiState
}
