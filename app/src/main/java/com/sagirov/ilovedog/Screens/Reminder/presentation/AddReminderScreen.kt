package com.sagirov.ilovedog

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.Activities.MainActivity.MainActivity
import com.sagirov.ilovedog.Screens.Reminder.domain.model.ReminderEntity
import com.sagirov.ilovedog.Screens.Reminder.presentation.ReminderViewModel
import com.sagirov.ilovedog.ServicesAndReceivers.NotificationReceiver
import com.sagirov.ilovedog.Utils.PreferencesUtils
import com.sagirov.ilovedog.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddReminderScreen(navController: NavController, viewModel: ReminderViewModel = hiltViewModel()/*, viewModel: ReminderViewModel*/) {

    val mContext = LocalContext.current
    val prefs = PreferencesUtils(mContext)


//    var dateForVisitToVet = mutableMapOf<Long, String>()
    var reason =  remember {mutableStateOf("")}
    var calendarDayText =  remember {mutableStateOf((Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1).toString())}
    var calendarMonthText = remember {mutableStateOf((Calendar.getInstance().get(Calendar.MONTH) + 1).toString())}
    var calendarYearText =  remember {mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())}
    var calendarHourText =  remember {mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))}
    var calendarMinuteText =  remember {mutableStateOf(Calendar.getInstance().get(Calendar.MINUTE))}
    var calendarHourTextModified =  remember {mutableStateOf(calendarHourText.value.toString())}
    var calendarMinuteTextModified = remember {mutableStateOf(calendarMinuteText.value.toString())}
    if (calendarHourText.value < 10) {calendarHourTextModified.value = "0"+calendarHourText.value.toString()}
    if (calendarMinuteText.value < 10) {calendarMinuteTextModified.value = "0"+calendarMinuteText.value.toString()}

//    val getArrayFromJson = prefs.getString(PREF_NAME_DATES,"dateForVisitToVet", "")
//    if (getArrayFromJson != "") {
//        dateForVisitToVet = (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
//    }

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
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    Column(
        Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mContext.resources.getString(R.string.reminder_activity_text) + ":",
            fontSize = 15.sp,
            modifier = Modifier //Text
                .fillMaxWidth()
                .padding(start = 75.dp),
            textAlign = TextAlign.Start,
            color = mainTextColor
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp)
                .focusRequester(reasonFocusRequester), colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = mainTextColor,
                focusedLabelColor = mainTextColor,
                cursorColor = mainTextColor,
                textColor = mainTextColor,
                unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
            ), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { dayFocusRequester.requestFocus() }),
//            label = { Text(text = "Причина:", fontSize = 15.sp)},
            value = reason.value, onValueChange = {
                if (it.length <= 150) {
                    reason.value = it
                }
            }, textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium),
            singleLine = true
        )
        Text(
            text = reason.value.length.toString() + "/150", fontSize = 15.sp, modifier = Modifier
                .fillMaxWidth()
                .padding(end = 60.dp), textAlign = TextAlign.End, color = mainTextColor
        )
        Row() {
            Card(
                onClick = {
                    calendarHourText.value += 1; if (calendarHourText.value > 24) {
                    calendarHourText.value = 0; calendarHourTextModified.value = "00"
                };
                    calendarHourTextModified.value =
                        calendarHourText.value.toString();if (calendarHourText.value < 10) {
                    calendarHourTextModified.value = "0" + calendarHourText.value.toString()
                }
                },
                backgroundColor = mainSecondColor, shape = RoundedCornerShape(10)
            ) {
                Text(
                    text = calendarHourTextModified.value,
                    fontSize = 120.sp,
                    color = mainTextColor
                )
            }
            Text(text = ":", fontSize = 120.sp, color = mainTextColor)
            Card(
                onClick = {
                    calendarMinuteText.value += 5
                    calendarMinuteTextModified.value = calendarMinuteText.value.toString()
                    if (calendarMinuteText.value > 60) {
                        calendarMinuteText.value = 0; calendarMinuteTextModified.value = "00"
                    }
                    if (calendarMinuteTextModified.value.toInt() < 10) {
                        calendarMinuteTextModified.value = "0${calendarMinuteText.value}"
                    }
                },
                backgroundColor = mainSecondColor, shape = RoundedCornerShape(10)
            )
            {
                Text(
                    text = calendarMinuteTextModified.value,
                    fontSize = 120.sp,
                    color = mainTextColor
                )
            }
        }

        val datePickerDialog = DatePickerDialog(mContext,
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
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = mainTextColor,
                    focusedLabelColor = mainTextColor,
                    cursorColor = mainTextColor,
                    textColor = mainTextColor,
                    unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                ),
                textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium),
                singleLine = true,
                label = {
                    Text(
                        text = mContext.resources.getString(R.string.reminder_activity_day_text),
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                }, //D
                value = calendarDayText.value,
                keyboardActions = KeyboardActions(onNext = {
                    monthFocusRequester.requestFocus()
                }),
                onValueChange = {
                    calendarDayText.value = it;
                    if (calendarDayText.value.length >= 2) {
                        if (calendarDayText.value.toInt() > days || calendarDayText.value.toInt() < 0) {
                            calendarDayText.value = days.toString()
                        }; monthFocusRequester.requestFocus()
                    }
                })
            TextField(modifier = Modifier
                .focusRequester(monthFocusRequester)
                .width((screenWidth / 5).dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = mainTextColor,
                    focusedLabelColor = mainTextColor,
                    cursorColor = mainTextColor,
                    textColor = mainTextColor,
                    unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                ),
                textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium),
                singleLine = true,
                label = {
                    Text(
                        text = mContext.resources.getString(R.string.reminder_activity_month_text),
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                }, //M
                value = calendarMonthText.value,
                onValueChange = {
                    calendarMonthText.value = it; if (calendarMonthText.value.length >= 2) {
                    if (calendarMonthText.value.toInt() > 13 || calendarMonthText.value.toInt() < 0) {
                        calendarMonthText.value = 12.toString()
                    };
                }
                })
            TextField(modifier = Modifier
                .width((screenWidth / 5).dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = mainTextColor,
                    focusedLabelColor = mainTextColor,
                    cursorColor = mainTextColor,
                    textColor = mainTextColor,
                    unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                ),
                textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium),
                singleLine = true,
                label = {
                    Text(
                        text = mContext.resources.getString(R.string.reminder_activity_year_text),
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                }, //YEAR
                value = calendarYearText.value,
                onValueChange = {
                    if (calendarYearText.value.length < 4) calendarYearText.value = it
                })
            IconButton(onClick = { datePickerDialog.show()}) {
                Icon(Icons.Outlined.DateRange, contentDescription = "", tint = mainTextColor)
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                mContext.resources.getString(R.string.reminder_activity_reminder_text),
                fontSize = 15.sp,
                color = mainTextColor
            ) // reminder
            Switch(
                checked = checkNotification.value,
                onCheckedChange = { checkNotification.value = it },
                colors = SwitchDefaults.colors(checkedThumbColor = switcherColor)
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp, top = 10.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                mContext.resources.getString(R.string.reminder_activity_repeat_text),
                fontSize = 15.sp,
                color = mainTextColor
            ) //Repeat
            Switch(
                checked = checkRepeat.value, onCheckedChange = { checkRepeat.value = it },
                colors = SwitchDefaults.colors(checkedThumbColor = switcherColor)
            )
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
//                prefs = getSharedPreferences(PREF_NAME_DATES, ComponentActivity.MODE_PRIVATE)
                    if (checkRepeat.value) {
                        viewModel.insert(
                            ReminderEntity(
                                0,
                                mapOf((System.currentTimeMillis() + time).toString() to reason.value + ".Повтор%#%:#%:")
                            )
                        )
//                        dateForVisitToVet[(System.currentTimeMillis()+time)] = reason.value+".Повтор%#%:#%:"
                    } else {
                        viewModel.insert(
                            ReminderEntity(
                                0,
                                mapOf((System.currentTimeMillis() + time).toString() to reason.value)
                            )
                        )
//                        dateForVisitToVet[(System.currentTimeMillis()+time)] = reason.value
                    }
//                    val json: String = Gson().toJson(dateForVisitToVet)
//                    prefs.putString(PreferencesUtils.Companion.PREF_NAME_DATES,"dateForVisitToVet",json)
//                prefs.edit().putString("dateForVisitToVet", json).apply()
                    when {
                        checkNotification.value && checkRepeat.value -> {val am = mContext.getSystemService(
                            Activity.ALARM_SERVICE) as AlarmManager
                            val intent = Intent(mContext, NotificationReceiver::class.java)
                            intent.putExtra("notification", reason.value)
                            val pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent,0)
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, createdAt.time, 86400000, pendingIntent)
                            Log.d("check", "checkNotification.value && checkRepeat.value")}
                        checkNotification.value -> {val am = mContext.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                            val intent = Intent(mContext, NotificationReceiver::class.java)
                            intent.putExtra("notification", reason.value)
                            val pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent,0)
                            //уведы срабатывают за пол дня до начала
                            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,(System.currentTimeMillis()+time)-3600000, pendingIntent)
                            Log.d("check", "checkNotification.value")}
                        checkRepeat.value -> {val am = mContext.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                            val pendingIntent = PendingIntent.getActivity(mContext, 1, Intent(mContext, MainActivity::class.java),0)
                            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, createdAt.time, 86400000, pendingIntent)
                            Log.d("check", "checkRepeat.value")}
                    }
                    navController.navigate("health")
//                startActivity(Intent(mContext, MainActivity::class.java))
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
            colors = ButtonDefaults.buttonColors(
                backgroundColor = mainSecondColor,
                contentColor = Color.Black
            )
        ) {
            Text(
                mContext.resources.getString(R.string.reminder_activity_button_add_reminder_text),
                color = mainTextColor
            ) //Add reminder
        }
    }
}