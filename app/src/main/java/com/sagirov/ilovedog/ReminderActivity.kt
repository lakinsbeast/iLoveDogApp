package com.sagirov.ilovedog

import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import java.util.*
import kotlin.time.Duration.Companion.days


class ReminderActivity : ComponentActivity() {
    private val PREF_NAME = "dates"
    private lateinit var prefs: SharedPreferences

    private val dateForVisitToVet = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
//        val edit = prefs.edit()
        val getArrayFromJson = prefs.getString("dateForVisitToVet", "")
        if (getArrayFromJson != "") {
            dateForVisitToVet.addAll(Gson().fromJson(getArrayFromJson, Array<Long>::class.java).toList())
        }

        setContent {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                time()
                listDates(data = dateForVisitToVet)
            }
        }
    }
    @Composable
    fun listDates(data: List<Long>) {
        LazyColumn {
            items(data) { item ->
                Text(DateFormat.getDateInstance(DateFormat.FULL).format(System.currentTimeMillis()+item).toString())
            }
        }
    }
    @Composable
    fun time() {
        val mContext = LocalContext.current
        var createdAt = Date()
        val timestamp = createdAt.time
        val mCalendar = Calendar.getInstance()

        AndroidView(
            { CalendarView(it) },
            modifier = Modifier.wrapContentWidth(),
            update = { views ->
                views.setOnDateChangeListener { calendarView, year, month, days ->
                    mCalendar.set(Calendar.DATE,days)
                    createdAt = mCalendar.time
                    if (timestamp/100 < createdAt.time/100) {

                    }
                }
            }
        )
        OutlinedButton(onClick = {
             val cal = Calendar.getInstance()
//            val intent = Intent(Intent.ACTION_EDIT)
//            intent.setData(CalendarContract.Events.CONTENT_URI)
//            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, (System.currentTimeMillis())+((createdAt.time) - timestamp)-86400000)
//            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (System.currentTimeMillis())+((createdAt.time) - timestamp))
//            intent.putExtra("title", "A Test Event from android app")
//            startActivity(intent)

            if (timestamp/100 < createdAt.time/100) {
                var test: Long = (createdAt.time) - timestamp
                prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                val edit = prefs.edit()
                dateForVisitToVet.add(test)
                val json: String = Gson().toJson(dateForVisitToVet)
                edit.putString("dateForVisitToVet", json)
                edit.apply()
//                val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
//                val intent = Intent(this@ReminderActivity, NotificationReceiver::class.java)
//                val pendingIntent = PendingIntent.getBroadcast(this@ReminderActivity, 0, intent, 0)
//                am.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+test, pendingIntent)
                Toast.makeText(
                    mContext,
                    "Добавлено новое напоминание",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(mContext, "Вы не можете выбрать дату меньше текущей", Toast.LENGTH_LONG).show()
                }
            },
            Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
                .height(70.dp)
                .fillMaxWidth()
                .border(
                    width = 0.dp, color = Color.Black,
                    shape = RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
            Text("Добавить напоминание!")
        }

    }
}
