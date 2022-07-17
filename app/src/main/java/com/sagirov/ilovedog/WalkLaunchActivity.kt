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
import androidx.compose.foundation.shape.RoundedCornerShape
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
        var backgroundColor = mutableStateOf(Color(0xFFFFFFFF))
        var textColor = mutableStateOf(Color(0xFF000000))
        var cautionText = ""

        setContent {
            val res = (currentTimeInMinutes.toFloat() / (stopTimer))
            //TODO{Сделать смену цвета круга в зависимости от процента гуляния}
            val circularColor = remember { mutableStateOf(Color(0xFF3A5A40)) }
//            val availableProgressColor = when {
//                res < 0.35F -> Color(0xFFFB3640) //green
//                res < 0.75F -> Color(0xFFffca3a) //yellow
//                else -> Color(0xFF8ac926) //red
//            }
            Column(
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor.value),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Давайте погуляем!", fontSize = 24.sp, color = textColor.value)
                CircularProgressIndicator(
                    progress = (currentTimeInMinutes.toFloat() / (stopTimer)),
                    strokeWidth = 12.dp,
                    color = circularColor.value,
                    modifier = Modifier.size(250.dp)
                )
                Text(text = DateUtils.formatElapsedTime(currentTimeInMinutes / 1000).toString(), color = textColor.value)
                OutlinedButton(
                    onClick = {
                        if (!isStartTimer) {
                            startCount(); isStartTimer = true
                            circularColor.value = Color(0xFF588157)
                            backgroundColor.value = Color(0xFF344E41)
                            textColor.value = Color(0xFFFFFFFF)
                            cautionText = "Не нажимайте на кнопку назад и не выгружайте приложение из памяти"
                        } else {
                            backgroundColor.value = Color(0xFFFFFFFF); textColor.value = Color(0xFF000000)
                            circularColor.value = Color(0xFF3A5A40)
                            timer.cancel(); isStartTimer = false
                            cautionText = ""
                        }
                    },
                    Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                    ,shape = RoundedCornerShape(0),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ), contentPadding = PaddingValues(0.dp)
                ) {
                    if (!isStartTimer) {
                        Text("Run")
                    } else {
                        Text("Stop")
                    }
                }

//                Button(onClick = {
//                    if (!isStartTimer) {
//                        startCount(); isStartTimer = true
//                        circularColor.value = Color(0xFF588157)
//                        backgroundColor.value = Color(0xFF344E41)
//                        textColor.value = Color(0xFFFFFFFF)
//                        cautionText = "Не нажимайте на кнопку назад и не выгружайте приложение из памяти"
//                    } else {
//                        backgroundColor.value = Color(0xFFFFFFFF); textColor.value = Color(0xFF000000)
//                        circularColor.value = Color(0xFF3A5A40)
//                        timer.cancel(); isStartTimer = false
//                        cautionText = ""
//                    }
//                }) {
//                    if (!isStartTimer) {
//                        Text("Run")
//                    } else {
//                        Text("Stop")
//                    }
//                }
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
