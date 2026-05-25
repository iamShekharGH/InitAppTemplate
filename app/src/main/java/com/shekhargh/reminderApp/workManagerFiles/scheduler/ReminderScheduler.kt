package com.shekhargh.reminderApp.workManagerFiles.scheduler

import com.shekhargh.reminderApp.data.db.Reminder

interface ReminderScheduler {
    fun scheduleReminder(reminder: Reminder)
    fun cancelReminder(reminderId: Int)
}