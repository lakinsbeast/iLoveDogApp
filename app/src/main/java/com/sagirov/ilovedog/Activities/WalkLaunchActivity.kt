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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.Activities.MainActivity.MainActivity.Companion.myPetPaddockT
import com.sagirov.ilovedog.Activities.MainActivity.presentation.DogsInfoViewModel
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ServicesAndReceivers.TimerService
import com.sagirov.ilovedog.Utils.PreferencesUtils
import com.sagirov.ilovedog.ui.theme.circularColor
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

var currentTimeInMinutes: Long by mutableStateOf(0)
var stopTimer: Long by mutableStateOf(0)
var isStartTimer by mutableStateOf(false)
var timeToString by mutableStateOf("")

@AndroidEntryPoint
class WalkLaunchActivity : ComponentActivity() {
    private val PREF_SCORE = "multiplier"
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences
    private lateinit var timer: CountDownTimer



    private var startTimeForScore = 0L
    private var score = 0
    private var multiplier = 1

    private val statsCurrentTime = mutableStateOf(0L)
    private val statsConstTime = mutableStateOf(0L)

    @Inject
    lateinit var newPrefs: PreferencesUtils

    private var inCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.mipmap.dog_icon_new).setContentTitle("Прогулка началась").setContentText(
            "Осталось ещё " +
                    (currentTimeInMinutes / 60000).toString() + " минут"
        )
    val endCycleNotif = NotificationCompat.Builder(this, "channelID")
        .setSmallIcon(R.mipmap.dog_icon_new).setContentTitle("Всё, можете закругляться!")
        .setContentText("Завершить?")

    override fun onBackPressed() {
        if (isStartTimer) {
            score += (TimeUnit.MILLISECONDS.toMinutes(startTimeForScore) -
                    TimeUnit.MILLISECONDS.toMinutes(currentTimeInMinutes)).toInt() * multiplier
            newPrefs.putInt(PREF_SCORE, "score", score)
            Toast.makeText(this@WalkLaunchActivity, "Таймер остановлен", Toast.LENGTH_SHORT).show()
            timer.cancel()
            isStartTimer = false
        }
        val notifManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.cancel(0)
        /////////////слишком костыльно, потом поменяю
        myPetPaddockT.value = true
        myPetPaddockT.value = false
        ///////////
        super.onBackPressed()
    }

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newPrefs = PreferencesUtils(this)
        val id = intent.getIntExtra("id", 0)
        var idOfProfile: Int = id

        lifecycleScope.launch {
            dogsInfoViewModel.dogProfiles.collect {
                idOfProfile = it[id].id
                statsCurrentTime.value = it[id].currentTimeWalk
                statsConstTime.value = it[id].walkingTimeConst
                currentTimeInMinutes = statsCurrentTime.value
                startTimeForScore = currentTimeInMinutes
                stopTimer = statsConstTime.value
            }
//            dogsInfoViewModel.getAllDogsProfiles.flowWithLifecycle(
//                lifecycle,
//                Lifecycle.State.STARTED
//            ).onEach {
//                idOfProfile = it[id].id
//                statsCurrentTime.value = it[id].currentTimeWalk
//                statsConstTime.value = it[id].walkingTimeConst
//                currentTimeInMinutes = statsCurrentTime.value
//                startTimeForScore = currentTimeInMinutes
//                stopTimer = statsConstTime.value
//            }.launchIn(lifecycleScope)
        }

//        dogsInfoViewModel.getAllDogsProfiles.observe(this) {
//            idOfProfile = it[id].id
//            statsCurrentTime.value = it[id].currentTimeWalk
//            statsConstTime.value =  it[id].walkingTimeConst
//            currentTimeInMinutes = statsCurrentTime.value
//            startTimeForScore = currentTimeInMinutes
//            stopTimer = statsConstTime.value
//        }

        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        var backgroundColor = mutableStateOf(mainBackgroundColor)
        var textColor = mutableStateOf(mainTextColor)
        var buttonBackgroundColor = mutableStateOf(mainSecondColor)
        var cautionText = ""
        multiplier = newPrefs.getInt(PREF_SCORE, "multiplierScore", 1)
        score = newPrefs.getInt(PREF_SCORE, "score", 1)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val circularColor = remember { mutableStateOf(circularColor) }
            Column(
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor.value),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = resources.getString(R.string.walk_activity_go_run_text),
                    fontSize = 24.sp,
                    color = textColor.value
                ) //Давайте погуляем
                CircularProgressIndicator(
                    progress = (currentTimeInMinutes.toFloat() / (stopTimer)),
                    strokeWidth = 20.dp,
                    color = circularColor.value,
                    modifier = Modifier.size(350.dp)
                )
                Text(
                    text = DateUtils.formatElapsedTime(currentTimeInMinutes / 1000).toString(),
                    color = textColor.value
                )
                OutlinedButton(
                    onClick = {
                        if (!isStartTimer) {
                            inCycleNotif =
                                NotificationCompat.Builder(this@WalkLaunchActivity, "channelID")
                                    .setSmallIcon(R.mipmap.dog_icon_new)
                                    .setContentTitle("Прогулка началась").setContentText(
                                        "Осталось ещё " +
                                                (currentTimeInMinutes / 60000).toString() + " минут"
                                    )
                            startCount(); isStartTimer = true
                            buttonBackgroundColor.value = Color(0xFF7BAA7A)
                            circularColor.value = Color(0xFF588157)
                            backgroundColor.value = Color(0xFF344E41)
                            textColor.value = Color(0xFFFFFFFF)
                            cautionText =
                                resources.getString(R.string.walk_activity_caution_text) //Не нажимайте на кнопку назад и не выгружайте приложение из памяти
                        } else {
                            score += (TimeUnit.MILLISECONDS.toMinutes(startTimeForScore) -
                                    TimeUnit.MILLISECONDS.toMinutes(currentTimeInMinutes)).toInt() * multiplier
                            newPrefs.putInt(PREF_SCORE, "score", score)
                            dogsInfoViewModel.updateDogsTime(idOfProfile, currentTimeInMinutes)
                            backgroundColor.value = mainBackgroundColor; textColor.value =
                                mainTextColor
                            buttonBackgroundColor.value = mainSecondColor
                            circularColor.value = com.sagirov.ilovedog.ui.theme.circularColor
                            timer.cancel(); isStartTimer = false
                            cautionText = ""
                        }
                    },
                    Modifier
                        .height(70.dp)
                        .fillMaxWidth(),shape = RoundedCornerShape(0),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = buttonBackgroundColor.value,
                        contentColor = Color.Black
                    ), contentPadding = PaddingValues(0.dp)
                ) {
                    if (!isStartTimer) {
                        Text(
                            resources.getString(R.string.walk_activity_go_run_button_text),
                            color = mainTextColor
                        ) //Run
                    } else {
                        Text(
                            resources.getString(R.string.walk_activity_go_stop_button_text),
                            color = mainTextColor
                        ) //Stop
                    }
                }
                Text(text = cautionText, fontSize = 24.sp, color = Color.White, textAlign = TextAlign.Center)
            }
        }
    }

    private fun startCount() {
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
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
                timeToString = currentTimeInMinutes.toString()
            }
            override fun onFinish() {
                score += (TimeUnit.MILLISECONDS.toMinutes(startTimeForScore)).toInt() * multiplier
                newPrefs.putInt(PREF_SCORE, "score", score)
                currentTimeInMinutes = if (standartPaddock != null) {
                    if (standartPaddock.isNotEmpty()) {
                        standartPaddock.toLong()
                    } else {
                        3600000
                    }
                } else {
                    3600000
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
