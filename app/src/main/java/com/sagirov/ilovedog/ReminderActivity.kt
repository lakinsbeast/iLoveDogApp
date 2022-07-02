package com.sagirov.ilovedog

import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class ReminderActivity : ComponentActivity() {
    private val PREF_NAME_DATES = "dates"
    private lateinit var prefs: SharedPreferences

    private var dateForVisitToVet = mutableMapOf<Long, String>()
    var reason =  mutableStateOf("")
    var calendarDayText =  mutableStateOf("")
    var calendarMonthText =  mutableStateOf("")
    var calendarYearText =  mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendarDayText.value = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1).toString()
        calendarMonthText.value = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()

        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
        val getArrayFromJson = prefs.getString("dateForVisitToVet", "")
        if (getArrayFromJson != "") {
            dateForVisitToVet = (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
            Log.d("clenadr", dateForVisitToVet.toString())
            var it = dateForVisitToVet.iterator()
            while (it.hasNext()) {
                var item = it.next()
                if (item.key < System.currentTimeMillis()) {
                    it.remove()
                    val json: String = Gson().toJson(dateForVisitToVet)
                    prefs.edit().putString("dateForVisitToVet", json).apply()
                }
            }
        }

        setContent {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                time()
            }
        }
    }
    @Composable
    fun time() {
        val mContext = LocalContext.current
        var createdAt = Date()
        val timestamp = createdAt.time
        val mCalendar = Calendar.getInstance()


        val dayFocusRequester = FocusRequester()
        val monthFocusRequester = FocusRequester()
        val reasonFocusRequester = FocusRequester()

        var mycal = GregorianCalendar()
        var days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val screenWidth = LocalConfiguration.current.screenWidthDp


        OutlinedTextField(modifier = Modifier.padding(bottom = 10.dp).focusRequester(reasonFocusRequester).focusTarget(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = {
                dayFocusRequester.requestFocus()
            }),
            label = { Text(text = "Причина:", fontSize = 15.sp)}, value = reason.value, onValueChange = {reason.value = it},
            singleLine = true)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.Gray)) {

            TextField(modifier = Modifier
                .width((screenWidth / 5).dp)
                .background(Color.White).focusRequester(dayFocusRequester),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                ,singleLine = true,  label = { Text(text = "ДД", fontSize = 15.sp)},value = calendarDayText.value, keyboardActions = KeyboardActions(onNext = {
                    monthFocusRequester.requestFocus()
                }),
                onValueChange = { calendarDayText.value = it;
                    if (calendarDayText.value.length >= 2) {if (calendarDayText.value.toInt() > days || calendarDayText.value.toInt() < 0)
                    {calendarDayText.value = days.toString()}; monthFocusRequester.requestFocus()}
                })
            TextField(modifier = Modifier
                .focusRequester(monthFocusRequester)
                .width((screenWidth / 5).dp)
                .background(Color.White), colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                ,singleLine = true,label = { Text(text = "MM", fontSize = 15.sp)},value = calendarMonthText.value,
                onValueChange = { calendarMonthText.value = it; if (calendarMonthText.value.length >= 2)
                {if (calendarMonthText.value.toInt() > 13 || calendarMonthText.value.toInt() < 0) {calendarMonthText.value = 12.toString() };
                    }
                })
            TextField(modifier = Modifier
                .width((screenWidth / 5).dp)
                .background(Color.White), colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                singleLine = true,label = { Text(text = "YEAR", fontSize = 15.sp)},value = calendarYearText.value,
                onValueChange = { if (calendarYearText.value.length < 4) calendarYearText.value = it
                })
        }



        OutlinedButton(onClick = {
                if (reason.value.isNotEmpty() && calendarDayText.value.isNotEmpty() && calendarMonthText.value.isNotEmpty() && calendarYearText.value.isNotEmpty()) {
                    mCalendar.set(Calendar.DAY_OF_MONTH, calendarDayText.value.toInt())
                    mCalendar.set(Calendar.MONTH, calendarMonthText.value.toInt()-1)
                    mCalendar.set(Calendar.YEAR, calendarYearText.value.toInt())
                    createdAt = mCalendar.time
                    if (((timestamp / 100) < (createdAt.time / 100))) {
                        var time: Long = (createdAt.time) - timestamp
//                        Log.d("calendar", (timestamp / 100).toString())
                        /* TODO{добавить взятие времени и перевод в календарное время} */

                        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
                        dateForVisitToVet[(System.currentTimeMillis()+time)] = reason.value
                        Log.d("calendar", dateForVisitToVet.toString())
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()

                        val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                        val intent = Intent(this@ReminderActivity, NotificationReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(this@ReminderActivity, 1, intent,0)
                        am.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+time, pendingIntent)

                        Toast.makeText(
                            mContext,
                            "Добавлено новое напоминание",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@ReminderActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Log.d("timestamp", (timestamp / 100).toString())
                        Log.d("createdAt.time", (createdAt.time / 100).toString())
                        Toast.makeText(mContext, "Вы не можете выбрать дату меньше текущей", Toast.LENGTH_LONG).show()
                    }


                } else {
                    Toast.makeText(mContext, "Напишите причину!", Toast.LENGTH_LONG).show()
                }
            },
            Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp, top = 10.dp
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

//        AndroidView(
//            { CalendarView(it) },
//            modifier = Modifier.wrapContentWidth(),
//            update = { views ->
//                views.setOnDateChangeListener { calendarView, year, month, days ->
//                    mCalendar.set(Calendar.DATE,days)
//                    createdAt = mCalendar.time
//                    if (timestamp/100 < createdAt.time/100) {
//                    }
//                }
//                views.minDate = System.currentTimeMillis()
//            }
//        )


    }
}
