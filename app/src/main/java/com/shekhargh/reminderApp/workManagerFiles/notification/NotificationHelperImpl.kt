package com.shekhargh.reminderApp.workManagerFiles.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.workManagerFiles.channelId
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelperImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NotificationHelper {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun showNotification(reminder: Reminder) {

        val deeplink = Intent(Intent.ACTION_VIEW, "reminder://item/${reminder.id}".toUri())
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deeplink)
            getPendingIntent(
                reminder.id,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reminder")
            .setContentText(reminder.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(reminder.id, notification)
    }

    override fun showNotificationSummary(list: List<Reminder>) {
//        val summary = list.joinToString(separator = "\n") { "• ${it.title}" }
        val inboxStyle = NotificationCompat.InboxStyle()
        list.forEach { inboxStyle.addLine(it.title) }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("You have ${list.size} reminders")
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}