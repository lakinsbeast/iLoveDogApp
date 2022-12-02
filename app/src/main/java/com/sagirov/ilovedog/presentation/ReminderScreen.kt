package com.sagirov.ilovedog.presentation

import android.app.AlertDialog
import android.icu.text.DateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sagirov.ilovedog.NavGraphRoutes
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.domain.utils.TextUtils
import com.sagirov.ilovedog.domain.utils.theme.healthBarPastReminderColor
import com.sagirov.ilovedog.domain.utils.theme.mainBackgroundColor
import com.sagirov.ilovedog.domain.utils.theme.mainSecondColor
import com.sagirov.ilovedog.domain.utils.theme.mainTextColor

private var dateForVisitToVet = mutableMapOf<Long, String>()
private var pastReminderMap = mutableStateMapOf<Long, String>()
private var pastReminderIds = mutableListOf<Int>()
private var weeklyReminderMap = mutableStateMapOf<Long, String>()
private var weeklyReminderIds = mutableListOf<Int>()
private var otherReminderMapp = mutableStateMapOf<Long, String>()
private var otherReminderIds = mutableListOf<Int>()
private var dayReminderMap = mutableStateMapOf<Long, String>()
private var dayReminderIds = mutableListOf<Int>()
private var repeatReminderMap = mutableStateMapOf<Long, String>()
private var repeatReminderIds = mutableListOf<Int>()
//TODO{ДОДЕЛАТЬ, СДЕЛАТЬ ВСЕ В DAO, не добавлять все путем прохождения цикла}

var textUtils: TextUtils = TextUtils()

@Composable
fun ReminderScreen(navController: NavController, viewModel: ReminderViewModel = hiltViewModel()) {
    val ctx = LocalContext.current
    LaunchedEffect(rememberCoroutineScope()) {
        viewModel.reminders.collect { list ->
            dateForVisitToVet.clear()
            repeatReminderMap.clear()
            pastReminderMap.clear()
            dayReminderMap.clear()
            weeklyReminderMap.clear()
            otherReminderMapp.clear()
            list.forEach { listfeach ->
                listfeach.reminder.forEach {
                    //                    dateForVisitToVet[it.key.toLong()] = it.value
                    try {
                        when {
                            it.value.contains(".Повтор%#%:#%:") -> {
                                repeatReminderMap[it.key.toLong()] =
                                    it.value.substringBefore(".Повтор"); repeatReminderIds.add(
                                    listfeach.id
                                )
                            }
                            it.key.toLong() < System.currentTimeMillis() -> {
                                pastReminderMap[it.key.toLong()] = it.value
                                pastReminderIds.add(listfeach.id)
                            }
                            System.currentTimeMillis() + 86400000 > it.key.toLong() -> {
                                dayReminderMap[it.key.toLong()] =
                                    it.value; dayReminderIds.add(listfeach.id)
                            }
                            System.currentTimeMillis() + 604800000 > it.key.toLong() -> {
                                weeklyReminderMap[it.key.toLong()] =
                                    it.value; weeklyReminderIds.add(listfeach.id)
                            }
                            else -> {
                                otherReminderMapp[it.key.toLong()] = it.value;otherReminderIds.add(
                                    listfeach.id
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("excp", e.toString())
                    }
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = {
                navController.navigate(NavGraphRoutes.addReminder.route)
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
            colors = ButtonDefaults.buttonColors(
                backgroundColor = mainSecondColor,
                contentColor = Color.Black
            )
        ) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_button_text),
                color = mainTextColor
            ) //Добавить напоминание
        }
        if (repeatReminderMap.isNotEmpty()) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_reminder_repeat),
                color = mainTextColor
            ) //Повторяющиеся напоминания
            RepeatReminderColumn(repeatReminderIds, data = repeatReminderMap, viewModel)
        }
        if (dayReminderMap.isNotEmpty()) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_reminder_day),
                color = mainTextColor
            ) //Дневные напоминания
            DayReminderColumn(dayReminderIds, data = dayReminderMap, viewModel)
        }
        if (weeklyReminderMap.isNotEmpty()) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_reminder_week),
                color = mainTextColor
            ) //Недельные напоминания
            WeeklyReminderColumn(weeklyReminderIds, data = weeklyReminderMap, viewModel)
        }
        if (otherReminderMapp.isNotEmpty()) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_reminder_other),
                color = mainTextColor
            ) //Остальные напоминания
            OtherReminderLazyColumn(otherReminderIds, data = otherReminderMapp, viewModel)
        }
        if (pastReminderMap.isNotEmpty()) {
            Text(
                ctx.resources.getString(R.string.reminder_menu_reminder_past),
                color = mainTextColor
            ) //Прошедшие напоминания
            PastReminderColumn(pastReminderIds, data = pastReminderMap, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OtherReminderLazyColumn(
    id: List<Int>,
    data: Map<Long, String>,
    remindersViewModel: ReminderViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    LazyColumn {
        data.forEach {
            item(it.key) {
                val alertDelete = AlertDialog.Builder(ctx)
                alertDelete.setTitle("Удалить напоминание?")
                alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                alertDelete.setCancelable(true)
                alertDelete.setPositiveButton("Да") { _, _ ->
                    dateForVisitToVet.remove(it.key)
                    otherReminderMapp.remove(it.key)
                    remindersViewModel.delete(mapOf(it.key.toString() to it.value))
                }
                alertDelete.setNegativeButton("Нет") { dialog, _ ->
                    dialog.cancel()
                }
                Card(
                    onClick = {
                        val alert = AlertDialog.Builder(ctx)
//                        alert.setTitle(it.value)
                        alert.setMessage(it.value)
                        alert.setCancelable(true)
                        alert.setPositiveButton("Удалить") { _, _ ->
                            alertDelete.create().show()
                        }
                        alert.create().show()
                    },
                    Modifier.padding(top = 0.dp, bottom = 10.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(mainSecondColor)
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            textUtils.textReduces(it.value)
                            /*textReduces(it.value)*/,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = mainTextColor
                        )
                        Column {
                            Text(
                                text = DateFormat.getDateInstance(DateFormat.SHORT)
                                    .format(it.key).toString(), color = mainTextColor
                            )
                            Text(
                                text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                    .format(it.key).toString(), color = mainTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepeatReminderColumn(
    id: List<Int>,
    data: Map<Long, String>,
    remindersViewModel: ReminderViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    LazyColumn {
        data.forEach {
            item(it.key) {
                alertDialogsForReminders({
                    remindersViewModel.delete(mapOf(it.key.toString() to it.value + ".Повтор%#%:#%:"))
                    dateForVisitToVet.remove(it.key)
                    repeatReminderMap.remove(it.key)
                }, it.value, it.key)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun alertDialogsForReminders(forPositiveBtn: () -> Unit, value: Any, key: Any) {
    val ctx = LocalContext.current
    val alertDelete = AlertDialog.Builder(ctx)
    alertDelete.setTitle("Удалить напоминание?")
    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${value}?")
    alertDelete.setCancelable(true)
    alertDelete.setPositiveButton("Да") { _, _ ->
        forPositiveBtn.invoke()
    }
    alertDelete.setNegativeButton("Нет") { dialog, _ ->
        dialog.cancel()
    }
    Card(onClick = {
        val alert = AlertDialog.Builder(ctx)
//                        alert.setTitle(it.value)
        alert.setMessage(value as String)
        alert.setCancelable(true)
        alert.setPositiveButton("Удалить") { _, _ ->
            alertDelete.create().show()
        }
        alert.create().show()
    }, shape = RoundedCornerShape(0.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(mainSecondColor)
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                textUtils.textReduces(value as String),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = mainTextColor
            )
            Column {
                Text(
                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                        .format(key).toString(), color = mainTextColor
                )
                Text(
                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                        .format(key).toString(), color = mainTextColor
                )
            }
//                            }
        }
    }
}

@Composable
fun DayReminderColumn(
    id: List<Int>,
    data: Map<Long, String>,
    remindersViewModel: ReminderViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    LazyColumn {
        data.forEach {
            item(it.key) {
                alertDialogsForReminders({
                    remindersViewModel.delete(mapOf(it.key.toString() to it.value))
                    dateForVisitToVet.remove(it.key)
                    dayReminderMap.remove(it.key)
                }, it.value, it.key)
            }
        }
    }
}

@Composable
fun WeeklyReminderColumn(
    id: List<Int>,
    data: Map<Long, String>,
    remindersViewModel: ReminderViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    LazyColumn {
        data.forEach {
            item(it.key) {
                alertDialogsForReminders({
                    dateForVisitToVet.remove(it.key)
                    weeklyReminderMap.remove(it.key)
                    remindersViewModel.delete(mapOf(it.key.toString() to it.value))
                }, it.value, it.key)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PastReminderColumn(
    id: List<Int>,
    data: Map<Long, String>,
    remindersViewModel: ReminderViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    LazyColumn {
        data.forEach {
            item(it.key) {
                val alertDelete = AlertDialog.Builder(ctx)
                alertDelete.setTitle("Удалить напоминание?")
                alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                alertDelete.setCancelable(true)
                alertDelete.setPositiveButton("Да") { _, _ ->
                    dateForVisitToVet.remove(it.key)
                    pastReminderMap.remove(it.key)
                    remindersViewModel.delete(mapOf(it.key.toString() to it.value))
                }
                alertDelete.setNegativeButton("Нет") { dialog, _ ->
                    dialog.cancel()
                }
                Card(onClick = {
                    val alert = AlertDialog.Builder(ctx)
//                        alert.setTitle(it.value)
                    alert.setMessage(it.value)
                    alert.setCancelable(true)
                    alert.setPositiveButton("Удалить") { _, _ ->
                        alertDelete.create().show()
                    }
                    alert.create().show()
                }, shape = RoundedCornerShape(0.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(healthBarPastReminderColor)
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            textUtils.textReduces(it.value),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = mainTextColor.copy(alpha = 0.5f)
                        )
                        Column {
                            Text(
                                text = DateFormat.getDateInstance(DateFormat.SHORT)
                                    .format(it.key).toString(),
                                color = mainTextColor.copy(alpha = 0.5f)
                            )
                            Text(
                                text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                    .format(it.key).toString(),
                                color = mainTextColor.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}