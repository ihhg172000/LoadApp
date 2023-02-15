package com.udacity.ui

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.views.LoadingButton
import com.udacity.R
import com.udacity.models.DownloadStatus
import com.udacity.utils.sendDownloadNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            when (rg_options.checkedRadioButtonId) {
                R.id.option_glide -> {
                    custom_button.state = LoadingButton.State.LOADING
                    download(
                        getString(R.string.glide_image_loading_library_by_bump_tech),
                        URL_GLIDE
                    )
                }
                R.id.option_loadapp -> {
                    custom_button.state = LoadingButton.State.LOADING
                    download(
                        getString(R.string.loadapp_current_repository_by_udacity),
                        URL_LOADAPP
                    )
                }
                R.id.option_retrofit -> {
                    custom_button.state = LoadingButton.State.LOADING
                    download(
                        getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_square_inc),
                        URL_RETROFIT
                    )
                }
                else -> {
                    Toast.makeText(this, getString(R.string.select_file_to_download), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val extras = intent.extras
            val id = extras?.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
            val action = intent.action

            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query().apply {
                        setFilterById(id)
                    }
                    val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                custom_button.state = LoadingButton.State.NORMAL
                                notificationManager.sendDownloadNotification(context, title, id, DownloadStatus.COMPLETED)
                            } else {
                                custom_button.state = LoadingButton.State.NORMAL
                                notificationManager.sendDownloadNotification(context, title, id, DownloadStatus.FAILED)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun download(title: String, url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }

    companion object {
        private const val URL_GLIDE = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val URL_LOADAPP = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_RETROFIT = "https://github.com/square/retrofit/archive/ref/heads/master.zip"
    }

}
