package com.sagirov.ilovedog.domain.utils.ServicesAndReceivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.sagirov.ilovedog.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val name = intent?.getStringExtra("notification")
        val notificationManager = context?.getSystemService<NotificationManager>()
        val notificationChannel = NotificationChannel(
            "channel_id",
            "Напоминание о походе к ветеринару",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager?.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(context!!, "channel_id")
            .setSmallIcon(R.mipmap.dog_icon_new)
            .setContentTitle("Напоминание!")
            .setContentText(name)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
        notificationManager?.notify(1, notification.build())}


    }
