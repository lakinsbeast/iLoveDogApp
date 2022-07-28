package com.sagirov.ilovedog

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerService : Service() {
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences
    private lateinit var timer: CountDownTimer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val edit = prefsMyPet.edit()
        timer = object : CountDownTimer(currentTimeInMinutes, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("timerActivity", isStartTimer.toString())
                currentTimeInMinutes = millisUntilFinished
                edit.putString("mypetPaddock", currentTimeInMinutes.toString())
                edit.apply()
                timeToString = currentTimeInMinutes.toString()
                if (currentTimeInMinutes < 1000) {
                    currentTimeInMinutes = 0
                    timeToString = currentTimeInMinutes.toString()
                    cancel()
                }
            }
            override fun onFinish() {

            }
        }.start()
        Toast.makeText(applicationContext, "Таймер продолжит работать на фоне", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Toast.makeText(applicationContext, "service restarted", Toast.LENGTH_SHORT).show()
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
//        Toast.makeText(applicationContext, "service destroyed", Toast.LENGTH_SHORT).show()
        timer.cancel()
        super.onDestroy()
    }

}