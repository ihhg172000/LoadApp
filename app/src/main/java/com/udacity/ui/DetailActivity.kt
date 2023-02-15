package com.udacity.ui

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.models.DownloadStatus
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FILE_TITLE = "file_title"
        const val EXTRA_DOWNLOAD_ID = "download_id"
        const val EXTRA_DOWNLOAD_STATUS = "download_status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        intent.extras?.apply {
            val fileTitle = getString(EXTRA_FILE_TITLE)
            val downloadId = getLong(EXTRA_DOWNLOAD_ID)
            val downloadStatus = DownloadStatus.values()[getInt(EXTRA_DOWNLOAD_STATUS)]

            tv_title.text = fileTitle
            tv_status.text =
                if (downloadStatus == DownloadStatus.COMPLETED) {
                    getString(R.string.download_completed)
                } else {
                    getString(R.string.download_failed)
                }

            tv_status.setTextColor(
                if (downloadStatus == DownloadStatus.COMPLETED) {
                    Color.GREEN
                } else {
                    Color.RED
                }
            )

            notificationManager.cancel(downloadId.toInt())
        }

        btn_ok.setOnClickListener {
            finish()
        }
    }
}
