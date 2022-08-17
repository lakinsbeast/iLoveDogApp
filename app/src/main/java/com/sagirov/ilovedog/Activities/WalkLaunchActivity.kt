package com.sagirov.ilovedog.Activities

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
import androidx.activity.viewModels
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
import com.sagirov.ilovedog.Activities.MainActivity.Companion.myPetPaddockT
import com.sagirov.ilovedog.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoViewModelFactory
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.TimerService
import com.sagirov.ilovedog.ui.theme.circularColor
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

var currentTimeInMinutes: Long by mutableStateOf(0)
var stopTimer: Long by mutableStateOf(0)
var isStartTimer by mutableStateOf(false)
var timeToString by mutableStateOf("")

@AndroidEntryPoint
class WalkLaunchActivity : ComponentActivity() {

    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences
    private lateinit var timer: CountDownTimer

    private val statsCurrentTime = mutableStateOf(0L)
    private val statsConstTime = mutableStateOf(0L)

    private val inCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Прогулка началась").setContentText("Осталось ещё "+
            (currentTimeInMinutes /60000).toString()+" минут")
    val endCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Всё, можете закругляться!")
        .setContentText("Завершить?")

    override fun onBackPressed() {
        if (isStartTimer){
            Toast.makeText(this@WalkLaunchActivity, "Таймер остановлен", Toast.LENGTH_SHORT).show()
            timer.cancel()
            isStartTimer = false
        }
        val notifManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.cancel(0)
        myPetPaddockT.value = true
        myPetPaddockT.value = false
        super.onBackPressed()}

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels {
        DogsInfoViewModelFactory((application as DogsApplication).DogsInfoRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id", 0)
        var idOfProfile: Int = id
        dogsInfoViewModel.getAllDogsProfiles.observe(this) {
            idOfProfile = it[id].id
            statsCurrentTime.value = it[id].currentTimeWalk
            statsConstTime.value =  it[id].walkingTimeConst
            currentTimeInMinutes = statsCurrentTime.value
            stopTimer = statsConstTime.value
        }

        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val myPetPaddock = prefsMyPet.getString("mypetPaddock", "")
        val myPetPaddockStandart = prefsMyPet.getString("mypetPaddockStandart", "")
//        Log.d("myPetPaddock", myPetPaddock.toString())
//        currentTimeInMinutes = myPetPaddock!!.toLong()
        var backgroundColor = mutableStateOf(mainBackgroundColor)
        var textColor = mutableStateOf(mainTextColor)
        var buttonBackgroundColor = mutableStateOf(mainSecondColor)
        var cautionText = ""

        setContent {
            val circularColor = remember { mutableStateOf(circularColor) }
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
                    strokeWidth = 20.dp,
                    color = circularColor.value,
                    modifier = Modifier.size(350.dp)
                )
                Text(text = DateUtils.formatElapsedTime(currentTimeInMinutes / 1000).toString(), color = textColor.value)
                OutlinedButton(
                    onClick = {
                        if (!isStartTimer) {
                            startCount(); isStartTimer = true
                            buttonBackgroundColor.value = Color(0xFF7BAA7A)
                            circularColor.value = Color(0xFF588157)
                            backgroundColor.value = Color(0xFF344E41)
                            textColor.value = Color(0xFFFFFFFF)
                            cautionText = "Не нажимайте на кнопку назад и не выгружайте приложение из памяти"
                        } else {
                            dogsInfoViewModel.updateDogsTime(idOfProfile, currentTimeInMinutes)
                            backgroundColor.value = mainBackgroundColor; textColor.value = mainTextColor
                            buttonBackgroundColor.value = mainSecondColor
                            circularColor.value = com.sagirov.ilovedog.ui.theme.circularColor
                            timer.cancel(); isStartTimer = false
                            cautionText = ""
                        }
                    },
                    Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                    ,shape = RoundedCornerShape(0),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = buttonBackgroundColor.value,
                        contentColor = Color.Black
                    ), contentPadding = PaddingValues(0.dp)
                ) {
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
        val standartPaddock = prefsMyPet.getString("mypetPaddockStandart", "")

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
//                edit.putString("mypetPaddock", currentTimeInMinutes.toString()).apply()
                timeToString = currentTimeInMinutes.toString()
            }
            override fun onFinish() {
                if (standartPaddock != null) {
                    if (standartPaddock.isNotEmpty()) {
                        currentTimeInMinutes = standartPaddock!!.toLong()
                    } else {
                        currentTimeInMinutes = 3600000
                    }
                } else {
                    currentTimeInMinutes = 3600000
                }
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
