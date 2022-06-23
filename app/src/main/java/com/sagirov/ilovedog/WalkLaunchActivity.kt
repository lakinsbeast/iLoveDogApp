package com.sagirov.ilovedog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import java.time.YearMonth

var currentTimeInMinutes: Long by mutableStateOf(3600000)
var stopTimer: Long by mutableStateOf(3600000)
var isStartTimer by mutableStateOf(false)
var timeToString by mutableStateOf("")
class WalkLaunchActivity : ComponentActivity() {
    private val PREF_NAME = "timer"

    private lateinit var prefs: SharedPreferences
    private lateinit var timer: CountDownTimer

    val inCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Гуляем...").setContentText("Осталось "+
            (currentTimeInMinutes/60000).toString()+" минут")
    val endCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Всё, животное нагулялось, можете завершать!")
        .setContentText("Завершить?")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        setContent {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Давайте погуляем!", fontSize = 24.sp)
                CircularProgressIndicator(
                    progress = (currentTimeInMinutes.toFloat() / (stopTimer)),
                    strokeWidth = 12.dp,
                    color = Color.Blue,
                    modifier = Modifier.size(250.dp)
                )
                Text(text = DateUtils.formatElapsedTime(currentTimeInMinutes / 1000).toString())
                Button(onClick = {
                    if (!isStartTimer) {
                        startCount(); isStartTimer = true
                    } else {
                        timer.cancel(); isStartTimer = false
                    }
                }) {
                    if (!isStartTimer) {
                        Text("Run")
                    } else {
                        Text("Stop")
                    }
                }
            }
        }
    }


    fun startCount() {
        val mNotifMan = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("channelID", "readable title", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        mNotifMan.createNotificationChannel(channel)
        inCycleNotif.setChannelId("channelID")
        endCycleNotif.setChannelId("channelID")
        mNotifMan.notify(0, inCycleNotif.build())
        timer = object : CountDownTimer(currentTimeInMinutes, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimeInMinutes = millisUntilFinished
                timeToString = currentTimeInMinutes.toString()
            }
            override fun onFinish() {
                currentTimeInMinutes = 3600000
                mNotifMan.notify(0, endCycleNotif.build())
                cancel()
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        stopService(Intent(this, TimerService::class.java))
    }

    override fun onStop() {
        super.onStop()
        if (isStartTimer){
            val intent = Intent(this, TimerService::class.java)
            startService(intent)
        }
    }
}
