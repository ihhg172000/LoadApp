package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.udacity.models.DownloadStatus
import com.udacity.R
import com.udacity.ui.DetailActivity

const val DOWNLOAD_CHANNEL_ID = "downloads"
const val DOWNLOAD_CHANNEL_TITLE = "Downloads"

fun NotificationManager.sendDownloadNotification(context: Context, fileTitle: String, downloadId: Long, downloadStatus: DownloadStatus) {
    val intent = Intent(context, DetailActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val bundle = Bundle().apply {
            putString(DetailActivity.EXTRA_FILE_TITLE, fileTitle)
            putLong(DetailActivity.EXTRA_DOWNLOAD_ID, downloadId)
            putInt(DetailActivity.EXTRA_DOWNLOAD_STATUS, downloadStatus.ordinal)
        }
        putExtras(bundle)
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        downloadId.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val notification = NotificationCompat.Builder(
        context,
        DOWNLOAD_CHANNEL_ID
    ).apply {
        setSmallIcon(R.drawable.ic_assistant_black_24dp)
        setContentTitle(fileTitle)
        setContentText(
            when(downloadStatus) {
                DownloadStatus.COMPLETED -> context.getString(R.string.download_completed)
                DownloadStatus.FAILED -> context.getString(R.string.download_failed)
            }
        )
        priority = NotificationManager.IMPORTANCE_DEFAULT
        addAction(
            NotificationCompat.Action(
                null,
                context.getString(R.string.details),
                pendingIntent
            )
        )
    }.build()
    notify(downloadId.toInt(), notification)
}