package com.sagirov.ilovedog.Activities

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ViewModels.VaccinationViewModel
import com.sagirov.ilovedog.ViewModels.VaccinationViewModelFactory
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import com.sagirov.ilovedog.ui.theme.textFieldUnFocusedIndicatorColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewVaccineActivity : ComponentActivity() {
    private var nameVaccination = mutableStateOf("")
    private var drugName = mutableStateOf("")
    private var date = mutableStateOf("")

    val mCalendar = Calendar.getInstance()
    private var calendarDayText =  mutableStateOf((Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1).toString())
    private var calendarMonthText =  mutableStateOf((Calendar.getInstance().get(Calendar.MONTH) + 1).toString())
    private var calendarYearText =  mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())

    private val vaccineViewModel: VaccinationViewModel by viewModels {
        VaccinationViewModelFactory((application as DogsApplication).VaccinationAppRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val screenWidth = LocalConfiguration.current.screenWidthDp
            var days = GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                R.style.light_dialog_theme,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendarDayText.value = dayOfMonth.toString()
                    calendarMonthText.value = (month + 1).toString()
                    calendarYearText.value = year.toString()
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(label = {
                    Text(
                        text = "Название прививки:",
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                },
                    value = nameVaccination.value,
                    onValueChange = { nameVaccination.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = mainTextColor,
                        focusedLabelColor = mainTextColor,
                        cursorColor = mainTextColor,
                        textColor = mainTextColor,
                        unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                    ),
                    singleLine = true
                )
                TextField(label = {
                    Text(
                        text = "Название лекарства:",
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                },
                    value = drugName.value,
                    onValueChange = { drugName.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = mainTextColor,
                        focusedLabelColor = mainTextColor,
                        cursorColor = mainTextColor,
                        textColor = mainTextColor,
                        unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                    ),
                    singleLine = true
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.clip(shape = RoundedCornerShape(5.dp))
                ) {
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
                        label = { Text(text = "Д", fontSize = 17.sp, color = mainTextColor) },
                        value = calendarDayText.value,
                        onValueChange = {
                            calendarDayText.value = it;
                            if (calendarDayText.value.length >= 2) {
                                if (calendarDayText.value.toInt() > days || calendarDayText.value.toInt() < 0) {
                                    calendarDayText.value = days.toString()
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
                        label = { Text(text = "M", fontSize = 17.sp, color = mainTextColor) },
                        value = calendarMonthText.value,
                        onValueChange = {
                            calendarMonthText.value = it; if (calendarMonthText.value.length >= 2) {
                            if (calendarMonthText.value.toInt() > 13 || calendarMonthText.value.toInt() < 0) {
                                calendarMonthText.value = 12.toString()
                            }; }
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
                        label = { Text(text = "ГОД", fontSize = 17.sp, color = mainTextColor) },
                        value = calendarYearText.value,
                        onValueChange = {
                            if (calendarYearText.value.length < 4) calendarYearText.value = it
                        })
                    IconButton(onClick = { datePickerDialog.show()}) {
                        Icon(
                            Icons.Outlined.DateRange,
                            contentDescription = "",
                            tint = mainTextColor
                        )
                    }
                }
                OutlinedButton(onClick = {
                    if (nameVaccination.value.isNotEmpty() && drugName.value.isNotEmpty()) {
                        mCalendar.set(Calendar.DAY_OF_MONTH, calendarDayText.value.toInt())
                        mCalendar.set(Calendar.MONTH, calendarMonthText.value.toInt()-1)
                        mCalendar.set(Calendar.YEAR, calendarYearText.value.toInt())
                        val createdAt = mCalendar.time
//                    if(((Date().time/100) < (createdAt.time/100))) {
                        vaccineViewModel.insertVaccination(VaccinationsEntity(0, nameVaccination.value, drugName.value, createdAt.time))
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
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainSecondColor,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Добавить вакцинацию", color = mainTextColor)
                }
            }
    }
    }
}
