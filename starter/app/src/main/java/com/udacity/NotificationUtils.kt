package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

private const val NOTIFICATION_ID = 1201
private const val REQUEST_CODE = 0

const val EXTRA_CODE_FILE_NAME = "EXTRA_CODE_FILE_NAME"
const val EXTRA_CODE_STATUS = "EXTRA_CODE_STATUS"

fun NotificationManager.sendNotification(
    applicationContext: Context,
    fileName: String,
    isSuccess: Boolean
) {

    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra(EXTRA_CODE_FILE_NAME, fileName)
    intent.putExtra(EXTRA_CODE_STATUS, isSuccess)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        intent,
        PendingIntent.FLAG_ONE_SHOT
    )

    // Build the notification
    val notification = NotificationCompat.Builder(applicationContext, MainActivity.CHANNEL_ID)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentText(applicationContext.getString(R.string.download_complete))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .addAction(
            R.drawable.ic_launcher_background,
            applicationContext.getString(R.string.notif_button_label),
            pendingIntent
        )

    notify(NOTIFICATION_ID, notification.build())

}

fun cancelAllNotifications(context: Context) {
    val notificationManager: NotificationManager =
        ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
    notificationManager.cancelAll()
}
