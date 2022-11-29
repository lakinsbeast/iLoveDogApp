package com.sagirov.ilovedog.presentation

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.domain.utils.theme.mainBackgroundColor
import com.sagirov.ilovedog.domain.utils.theme.mainSecondColor
import com.sagirov.ilovedog.domain.utils.theme.mainTextColor
import com.sagirov.ilovedog.domain.utils.theme.textFieldUnFocusedIndicatorColor

@Composable
fun AddVaccination(navController: NavController, viewModel: VaccinationViewModel = hiltViewModel()) {
    val ctx = LocalContext.current

    val nameVaccination = remember {mutableStateOf("")}
    val drugName = remember { mutableStateOf("") }
    var date = remember { mutableStateOf("") }

    val mCalendar = Calendar.getInstance()
    val calendarDayText =  remember {
        mutableStateOf(
            (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1).toString()
        )
    }
    val calendarMonthText =  remember {
        mutableStateOf(
            (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        )
    }
    val calendarYearText =  remember {
        mutableStateOf(
            Calendar.getInstance().get(Calendar.YEAR).toString()
        )
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val days = GregorianCalendar().getActualMaximum(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(ctx,
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
                viewModel.insert(
                    VaccinationsEntity(
                        0,
                        nameVaccination.value,
                        drugName.value,
                        createdAt.time
                    )
                )
                navController.navigate("vaccination") {
                    popUpTo("home")
                }
//                    } else {
//                        Toast.makeText(this@NewVaccineActivity, "Со временем пизда", Toast.LENGTH_LONG).show()
//                    }
            } else {
                Toast.makeText(ctx, "Не все поля заполнены", Toast.LENGTH_LONG).show()
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