package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity(){

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            //no radio buttons selected
            if (radioGroup.checkedRadioButtonId == -1) {
                Toast
                    .makeText(this, getString(R.string.alert_no_file_selected),LENGTH_SHORT)
                    .show()
            } else {
                download()
                custom_button.isEnabled = false
            }
        }

        createChannel(
            CHANNEL_ID,
            getString(R.string.channel_name_download_complete)
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            custom_button.isEnabled = true
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification(
                context.getString(R.string.download_complete),
                context,
                getRepo(),
                true
            )

        }
    }

    private fun download() {

        val url = getUrl()
        if (url.isNotEmpty()) {
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        }

    }

    private fun getUrl(): String {
        return when(radioGroup.checkedRadioButtonId) {
            radioButtonGlide.id -> URL_GLIDE_REPO
            radioButtonCurrentRepo.id -> URL_CURRENT_REPO
            radioButtonRetrofitRepo.id -> URL_RETROFIT_REPO
            else -> ""
        }
    }

    private fun getRepo(): String {
        return when(radioGroup.checkedRadioButtonId) {
            radioButtonGlide.id -> getString(R.string.label_glide_download)
            radioButtonCurrentRepo.id -> getString(R.string.label_udacity_download)
            radioButtonRetrofitRepo.id -> getString(R.string.label_retrofit_download)
            else -> ""
        }
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.setShowBadge(false)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.description = getString(R.string.download_complete)
            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(
                    application,
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }


    companion object {

        private const val URL_GLIDE_REPO =
            "https://github.com/bumptech/glide/archive/master.zip"

        private const val URL_CURRENT_REPO =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val URL_RETROFIT_REPO =
            "https://github.com/square/retrofit/master.zip"

        const val CHANNEL_ID = "channelId"
    }

}
