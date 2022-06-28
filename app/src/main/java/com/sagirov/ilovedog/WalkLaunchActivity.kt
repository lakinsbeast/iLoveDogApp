package com.sagirov.ilovedog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat

var currentTimeInMinutes: Long by mutableStateOf(0)
var stopTimer: Long by mutableStateOf(0)
var isStartTimer by mutableStateOf(false)
var timeToString by mutableStateOf("")
class WalkLaunchActivity : ComponentActivity() {

    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences
    private lateinit var timer: CountDownTimer

    private val inCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Гуляем...").setContentText("Осталось ещё "+
            (currentTimeInMinutes/60000).toString()+" минут")
    val endCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Всё, можете закругляться!")
        .setContentText("Завершить?")

    override fun onBackPressed() {
        super.onBackPressed()
        if (isStartTimer) {
            
        } else {
        startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val myPetPaddock = prefsMyPet.getString("mypetPaddock", "")
        val myPetPaddockStandart = prefsMyPet.getString("mypetPaddockStandart", "")
        currentTimeInMinutes = myPetPaddock!!.toLong()
        stopTimer = myPetPaddockStandart!!.toLong()
        var backgroundColor = Color(0xFFFFFFFF)
        var textColor = Color(0xFF000000)
        var cautionText = ""

        setContent {
            Column(
                Modifier.fillMaxSize().background(backgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Давайте погуляем!", fontSize = 24.sp, color = textColor)
                CircularProgressIndicator(
                    progress = (currentTimeInMinutes.toFloat() / (stopTimer)),
                    strokeWidth = 12.dp,
                    color = Color.Blue,
                    modifier = Modifier.size(250.dp)
                )
                Text(text = DateUtils.formatElapsedTime(currentTimeInMinutes / 1000).toString(), color = textColor)
                Button(onClick = {
                    if (!isStartTimer) {
                        startCount(); isStartTimer = true
                        backgroundColor = Color(0xFF858585)
                        textColor = Color(0xFFFFFFFF)
                        cautionText = "Не нажимайте на кнопку назад и не выгружайте приложение из памяти"
                    } else {
                        backgroundColor = Color(0xFFFFFFFF)
                        timer.cancel(); isStartTimer = false
                    }
                }) {
                    if (!isStartTimer) {
                        Text("Run")
                    } else {
                        Text("Stop")
                    }
                }
                Text(text = cautionText, fontSize = 24.sp, color = Color.White, textAlign = TextAlign.Center)
            }
        }
    }

    private fun startCount() {
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val edit = prefsMyPet.edit()

        val mNotifMan = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("channelID", "readable title", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        mNotifMan.createNotificationChannel(channel)
        inCycleNotif.setChannelId("channelID")
        endCycleNotif.setChannelId("channelID")
//        mNotifMan.notify(0, inCycleNotif.build())

        timer = object : CountDownTimer(currentTimeInMinutes, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimeInMinutes = millisUntilFinished
                Log.d("timerActivity", currentTimeInMinutes.toString())
                Log.d("timerActivity1", stopTimer.toString())
                edit.putString("mypetPaddock", currentTimeInMinutes.toString()).apply()
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
        Log.d("timerActivity", "onResume")
        stopService(Intent(this, TimerService::class.java))
    }

    override fun onStop() {
        super.onStop()
        Log.d("timerActivity", "onStop")
        if (isStartTimer){
            val intent = Intent(this, TimerService::class.java)
            startService(intent)
        }
    }
}
