package com.shekhargh.reminderApp.workManagerFiles.notification

import com.shekhargh.reminderApp.data.db.Reminder

interface NotificationHelper {
    fun showNotification(reminder: Reminder)
    fun showNotificationSummary(list: List<Reminder>)
    fun areNotificationsEnabled(): Boolean
}