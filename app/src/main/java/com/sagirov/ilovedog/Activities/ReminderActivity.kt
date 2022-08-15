package com.sagirov.ilovedog.Activities

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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagirov.ilovedog.NotificationReceiver
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

// TODO{НЕРЕАЛЬНО СДЕЛАТЬ - Поменять смену времени с кликов на scroll wheel(придется делать самописный
//  https://developer.android.com/jetpack/compose/gestures#scrolling тут показан пример)}

@AndroidEntryPoint
class ReminderActivity : ComponentActivity() {

    private val PREF_NAME_DATES = "dates"
    private lateinit var prefs: SharedPreferences
//    private val hours = List(25) { it }
//    private val minutes = List(60) { it }

    private var dateForVisitToVet = mutableMapOf<Long, String>()
    var reason =  mutableStateOf("")
    private var calendarDayText =  mutableStateOf("")
    private var calendarMonthText =  mutableStateOf("")
    private var calendarYearText =  mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())
    private var calendarHourText =  mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
    private var calendarMinuteText =  mutableStateOf(Calendar.getInstance().get(Calendar.MINUTE))
    var calendarHourTextModified =  mutableStateOf(calendarHourText.value.toString())
    var calendarMinuteTextModified = mutableStateOf(calendarMinuteText.value.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendarDayText.value = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1).toString()
        calendarMonthText.value = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        if (calendarHourText.value < 10) {calendarHourTextModified.value = "0"+calendarHourText.value.toString()}
        if (calendarMinuteText.value < 10) {calendarMinuteTextModified.value = "0"+calendarMinuteText.value.toString()}

        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
        val getArrayFromJson = prefs.getString("dateForVisitToVet", "")
        if (getArrayFromJson != "") {
            dateForVisitToVet = (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
            Log.d("clenadr", dateForVisitToVet.toString())
//            var it = dateForVisitToVet.iterator()
//            while (it.hasNext()) {
//                var item = it.next()
////                if (item.key < System.currentTimeMillis()) {
////                    it.remove()
////                    val json: String = Gson().toJson(dateForVisitToVet)
////                    prefs.edit().putString("dateForVisitToVet", json).apply()
////                }
//            }
        }

        setContent {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor)
                    .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                time()
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun time() {
        val mContext = LocalContext.current
        var createdAt = Date()
        val timestamp = createdAt.time
        val mCalendar = Calendar.getInstance()

        val checkNotification = remember { mutableStateOf(false) }
        val checkRepeat = remember { mutableStateOf(false) }

        val dayFocusRequester = FocusRequester()
        val monthFocusRequester = FocusRequester()
        val reasonFocusRequester = FocusRequester()

        var mycal = GregorianCalendar()
        var days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val screenWidth = LocalConfiguration.current.screenWidthDp

        Text(text = "Текст:", fontSize = 15.sp, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 75.dp), textAlign = TextAlign.Start)

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp)
            .focusRequester(reasonFocusRequester),colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
            focusedLabelColor = Color.Black, cursorColor = Color.Black), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { dayFocusRequester.requestFocus() }),
//            label = { Text(text = "Причина:", fontSize = 15.sp)},
            value = reason.value, onValueChange = { if (it.length <= 150) { reason.value = it } }, textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium),
            singleLine = true)
        Text(text = reason.value.length.toString()+"/150", fontSize = 15.sp, modifier = Modifier
            .fillMaxWidth()
            .padding(end = 60.dp), textAlign = TextAlign.End)
        Row() {
            Card(onClick = { calendarHourText.value += 1 ; if (calendarHourText.value > 24) {calendarHourText.value = 0 ; calendarHourTextModified.value = "00"};
                calendarHourTextModified.value = calendarHourText.value.toString();if (calendarHourText.value < 10) {calendarHourTextModified.value = "0"+calendarHourText.value.toString()}},) {
                Text(text = calendarHourTextModified.value, fontSize = 120.sp )
            }
            Text(text = ":", fontSize = 120.sp)
            Card(onClick = { calendarMinuteText.value += 5
                calendarMinuteTextModified.value = calendarMinuteText.value.toString()
                if (calendarMinuteText.value > 60) {calendarMinuteText.value = 0; calendarMinuteTextModified.value = "00"}
                if (calendarMinuteTextModified.value.toInt() < 10) {calendarMinuteTextModified.value = "0${calendarMinuteText.value}"}})
            {
                Text(text = calendarMinuteTextModified.value, fontSize = 120.sp)
            }
        }

        val datePickerDialog = DatePickerDialog(this,
            R.style.light_dialog_theme, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendarDayText.value = dayOfMonth.toString()
            calendarMonthText.value = (month + 1).toString()
            calendarYearText.value = year.toString()
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(5.dp))) {

            TextField(modifier = Modifier
                .width((screenWidth / 5).dp)
                .focusRequester(dayFocusRequester),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black, cursorColor = Color.Black),textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium)
                ,singleLine = true,  label = { Text(text = "Д", fontSize = 17.sp)},value = calendarDayText.value, keyboardActions = KeyboardActions(onNext = {
                    monthFocusRequester.requestFocus()
                }),
                onValueChange = { calendarDayText.value = it;
                    if (calendarDayText.value.length >= 2) {if (calendarDayText.value.toInt() > days || calendarDayText.value.toInt() < 0)
                    {calendarDayText.value = days.toString()}; monthFocusRequester.requestFocus()}
                })
            TextField(modifier = Modifier
                .focusRequester(monthFocusRequester)
                .width((screenWidth / 5).dp)
                , colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black, cursorColor = Color.Black),textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium)
                ,singleLine = true,label = { Text(text = "M", fontSize = 17.sp)},value = calendarMonthText.value,
                onValueChange = { calendarMonthText.value = it; if (calendarMonthText.value.length >= 2)
                {if (calendarMonthText.value.toInt() > 13 || calendarMonthText.value.toInt() < 0) {calendarMonthText.value = 12.toString() };
                    }
                })
            TextField(modifier = Modifier
                .width((screenWidth / 5).dp)
                , colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black, cursorColor = Color.Black), textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium),
                singleLine = true,label = { Text(text = "ГОД", fontSize = 17.sp)},value = calendarYearText.value,
                onValueChange = { if (calendarYearText.value.length < 4) calendarYearText.value = it })
            IconButton(onClick = { datePickerDialog.show()}) {
                Icon(Icons.Outlined.DateRange, contentDescription = "")
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Уведомления", fontSize = 15.sp)
            Switch(
                checked = checkNotification.value,
                onCheckedChange = { checkNotification.value = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.Black))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp, top = 10.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Text("Повтор", fontSize = 15.sp)
            Switch(checked = checkRepeat.value, onCheckedChange = {checkRepeat.value = it}, colors = SwitchDefaults.colors(checkedThumbColor = Color.Black))
        }
        OutlinedButton(onClick = {
                if (reason.value.isNotEmpty() && calendarDayText.value.isNotEmpty() && calendarMonthText.value.isNotEmpty() && calendarYearText.value.isNotEmpty()) {
                    mCalendar.set(Calendar.DAY_OF_MONTH, calendarDayText.value.toInt())
                    mCalendar.set(Calendar.MONTH, calendarMonthText.value.toInt()-1)
                    mCalendar.set(Calendar.YEAR, calendarYearText.value.toInt())
                    mCalendar.set(Calendar.HOUR_OF_DAY, calendarHourText.value.toInt())
                    mCalendar.set(Calendar.MINUTE, calendarMinuteText.value.toInt())

                    createdAt = mCalendar.time
                    if (((timestamp / 100) < (createdAt.time / 100))) {
                        var time: Long = (createdAt.time) - timestamp
                        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
                        if (checkRepeat.value) {
                            dateForVisitToVet[(System.currentTimeMillis()+time)] = reason.value+".Повтор%#%:#%:"
                        } else {
                            dateForVisitToVet[(System.currentTimeMillis()+time)] = reason.value
                        }
                        Log.d("calendar", dateForVisitToVet.toString())
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
                        when {
                            checkNotification.value && checkRepeat.value -> {val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                                val intent = Intent(this@ReminderActivity, NotificationReceiver::class.java)
                                intent.putExtra("notification", reason.value)
                                val pendingIntent = PendingIntent.getBroadcast(this@ReminderActivity, 1, intent,0)
                                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, createdAt.time, 86400000, pendingIntent)
                            Log.d("check", "checkNotification.value && checkRepeat.value")}
                            checkNotification.value -> {val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                                val intent = Intent(this@ReminderActivity, NotificationReceiver::class.java)
                                intent.putExtra("notification", reason.value)
                                val pendingIntent = PendingIntent.getBroadcast(this@ReminderActivity, 1, intent,0)
                                //уведы срабатывают за пол дня до начала
                                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,(System.currentTimeMillis()+time)-3600000, pendingIntent)
                                Log.d("check", "checkNotification.value")}
                            checkRepeat.value -> {val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                                val pendingIntent = PendingIntent.getActivity(this@ReminderActivity, 1, Intent(this@ReminderActivity, MainActivity::class.java),0)
                                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, createdAt.time, 86400000, pendingIntent)
                                Log.d("check", "checkRepeat.value")}
                        }
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
        }}}

