package com.shekhargh.reminderApp.workManagerFiles

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shekhargh.reminderApp.data.usecase.GetItemFromIdUseCase
import com.shekhargh.reminderApp.workManagerFiles.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getItemFromIdUseCase: GetItemFromIdUseCase,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Robust ID extraction: WorkManager may store numbers as Longs depending on source.
        val reminderId = when (val value = inputData.keyValueMap[reminderKeyText]) {
            is Int -> value
            is Long -> value.toInt()
            else -> inputData.getInt(reminderKeyText, -1)
        }

        if (reminderId == -1) return Result.failure()

        return when (val reminder = getItemFromIdUseCase(reminderId)) {
            com.shekhargh.reminderApp.data.usecase.Result.Empty -> Result.failure()
            is com.shekhargh.reminderApp.data.usecase.Result.Error -> Result.failure()
            is com.shekhargh.reminderApp.data.usecase.Result.Success -> {
                notificationHelper.showNotification(reminder.data)
                Result.success()
            }
        }
    }
}