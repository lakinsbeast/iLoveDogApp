package com.sagirov.ilovedog

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme
import java.util.*

class NewVaccineActivity : ComponentActivity() {
    private var nameVaccination = mutableStateOf("")
    private var drugName = mutableStateOf("")
    private var date = mutableStateOf("")

    val mCalendar = Calendar.getInstance()
    private var calendarDayText =  mutableStateOf((Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1).toString())
    private var calendarMonthText =  mutableStateOf((Calendar.getInstance().get(Calendar.MONTH) + 1).toString())
    private var calendarYearText =  mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())

    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            var days = GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.light_dialog_theme, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendarDayText.value = dayOfMonth.toString()
                calendarMonthText.value = (month + 1).toString()
                calendarYearText.value = year.toString()
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(label = { Text(text = "Название прививки:", fontSize = 15.sp)}, value = nameVaccination.value
                    , onValueChange = {nameVaccination.value = it}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black, focusedLabelColor = Color.Black, cursorColor = Color.Black), singleLine = true)
                TextField(label = { Text(text = "Название лекарства:", fontSize = 15.sp)}, value = drugName.value
                    , onValueChange = {drugName.value = it}, colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black, focusedLabelColor = Color.Black, cursorColor = Color.Black), singleLine = true)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.clip(shape = RoundedCornerShape(5.dp))) {
                    TextField(modifier = Modifier
                        .width((screenWidth / 5).dp),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black, cursorColor = Color.Black),textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium)
                        ,singleLine = true,  label = { Text(text = "Д", fontSize = 17.sp)},value = calendarDayText.value,
                        onValueChange = { calendarDayText.value = it;
                            if (calendarDayText.value.length >= 2) {if (calendarDayText.value.toInt() > days || calendarDayText.value.toInt() < 0)
                            {calendarDayText.value = days.toString()};}
                        })
                    TextField(modifier = Modifier
                        .width((screenWidth / 5).dp)
                        , colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black, cursorColor = Color.Black),textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Medium)
                        ,singleLine = true,label = { Text(text = "M", fontSize = 17.sp)},value = calendarMonthText.value,
                        onValueChange = { calendarMonthText.value = it; if (calendarMonthText.value.length >= 2)
                        {if (calendarMonthText.value.toInt() > 13 || calendarMonthText.value.toInt() < 0) {calendarMonthText.value = 12.toString() }; }
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
            OutlinedButton(onClick = {
                if (nameVaccination.value.isNotEmpty() && drugName.value.isNotEmpty()) {
                    mCalendar.set(Calendar.DAY_OF_MONTH, calendarDayText.value.toInt())
                    mCalendar.set(Calendar.MONTH, calendarMonthText.value.toInt()-1)
                    mCalendar.set(Calendar.YEAR, calendarYearText.value.toInt())
                    val createdAt = mCalendar.time
//                    if(((Date().time/100) < (createdAt.time/100))) {
                    dogsViewModel.insertVaccination(VaccinationsEntity(0, nameVaccination.value, drugName.value, createdAt.time))
                    finish()
//                    } else {
//                        Toast.makeText(this@NewVaccineActivity, "Со временем пизда", Toast.LENGTH_LONG).show()
//                    }
                } else {
                    Toast.makeText(this@NewVaccineActivity, "Не все поля заполнены", Toast.LENGTH_LONG).show()
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
                Text("Добавить вакцинацию!")
            }
        }
    }
    }
}