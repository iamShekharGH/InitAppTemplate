package com.shekhargh.reminderApp.workManagerFiles.scheduler

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.workManagerFiles.DailyReminderWorker
import com.shekhargh.reminderApp.workManagerFiles.reminderKeyText
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor(
    @param:ApplicationContext val context: Context
) : ReminderScheduler {

    override fun scheduleReminder(reminder: Reminder) {

        val delay = Duration.between(LocalDateTime.now(), reminder.dueDate).toMillis()
        if (delay <= 0) return

        val request = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .setInitialDelay(duration = delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(reminderKeyText to reminder.id))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName = "reminder_${reminder.id}",
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request
        )
    }

    override fun cancelReminder(reminderId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName = "reminder_$reminderId")
    }
}