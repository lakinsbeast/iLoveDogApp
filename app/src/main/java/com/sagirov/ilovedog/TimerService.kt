package com.sagirov.ilovedog

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast

class TimerService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        object : CountDownTimer(currentTimeInMinutes, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimeInMinutes = millisUntilFinished
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
        Toast.makeText(applicationContext, "This is a service running in background", Toast.LENGTH_SHORT).show()
        return START_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Toast.makeText(applicationContext, "service destroyed", Toast.LENGTH_SHORT).show()
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Toast.makeText(applicationContext, "service destroyed", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

}