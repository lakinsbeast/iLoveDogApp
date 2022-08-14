package com.sagirov.ilovedog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.content.getSystemService
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
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Напоминание!")
            .setContentText(name)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
        notificationManager?.notify(1, notification.build())}


    }


//    fun trigger(context: Context?) {
//        val notificationManager = context?.getSystemService<NotificationManager>()
//        val notificationChannel = NotificationChannel(
//            "channel_id",
//            "Напоминание о походе к ветеринару",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager?.createNotificationChannel(notificationChannel)
//        val notification = NotificationCompat.Builder(context!!, "channel_id")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Поход к ветеринару!")
//            .setContentText("Самое время пойти к ветеринару!")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_REMINDER)
//        notificationManager?.notify(1, notification.build())}
