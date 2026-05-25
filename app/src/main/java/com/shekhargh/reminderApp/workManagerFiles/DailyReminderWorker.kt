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

        val reminderId = inputData.getInt(reminderKeyText, -1)
        if (reminderId == -1) return Result.failure()

        when (val reminder = getItemFromIdUseCase(reminderId)) {
            com.shekhargh.reminderApp.data.usecase.Result.Empty -> return Result.failure()
            is com.shekhargh.reminderApp.data.usecase.Result.Error -> return Result.failure()
            is com.shekhargh.reminderApp.data.usecase.Result.Success -> {
                notificationHelper.showNotification(reminder.data)
                return Result.success()
            }
        }
    }
}