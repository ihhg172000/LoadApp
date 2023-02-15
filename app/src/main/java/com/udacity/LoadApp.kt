package com.udacity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.udacity.utils.DOWNLOAD_CHANNEL_ID
import com.udacity.utils.DOWNLOAD_CHANNEL_TITLE

class LoadApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createDownloadNotificationChannel()
    }

    private fun createDownloadNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                DOWNLOAD_CHANNEL_ID,
                DOWNLOAD_CHANNEL_TITLE,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}