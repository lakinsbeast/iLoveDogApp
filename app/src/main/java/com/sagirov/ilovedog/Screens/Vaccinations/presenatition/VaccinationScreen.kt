package com.sagirov.ilovedog.Screens.Vaccinations

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.NavGraphRoutes
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.Screens.Vaccinations.domain.model.VaccinationsEntity
import com.sagirov.ilovedog.Screens.Vaccinations.presenatition.VaccinationViewModel
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VaccinationScreen(navController: NavController, viewModel: VaccinationViewModel = hiltViewModel()) {
    val ctx = LocalContext.current

    var vaccinationList = remember { mutableStateListOf<VaccinationsEntity>() }
    var idOfColumn = 0
    var idOfVaccinationCard = 0
    val list = viewModel.vaccination.collectAsState(vaccinationList).value.toMutableList()
//    LaunchedEffect(rememberCoroutineScope()) {
//
//        viewModel.getAllVaccinations.collect {
//            if (it.isNotEmpty()) {
//                vaccinationList.clear()
//                vaccinationList.addAll(it)
//            }
//        }
//    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    val dialogDeleteVaccine = remember { mutableStateOf(false) }

    Scaffold(topBar = {}, floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate(NavGraphRoutes.addVaccination.route) {
                popUpTo(NavGraphRoutes.vaccination.route)
            }
        }, content = {
            Icon(
                Icons.Filled.Add,
                contentDescription = ""
            )
        }, backgroundColor = mainSecondColor)
    }) {
        if (dialogDeleteVaccine.value) {
            AlertDialog(onDismissRequest = {dialogDeleteVaccine.value = false}, buttons = {
                Column() {
                    OutlinedButton(
                        onClick = {
                            viewModel.delete(idOfVaccinationCard)
                            list.removeAt(idOfColumn)
                            idOfVaccinationCard = 0
                            idOfColumn = 0
                            dialogDeleteVaccine.value = false
                        },
                        Modifier
                            .height(60.dp)
                            .fillMaxWidth(),shape = RoundedCornerShape(0),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = mainSecondColor,
                            contentColor = Color.Black
                        ), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = ctx.resources.getString(R.string.vaccination_activity_delete),
                            color = mainTextColor
                        ) //Удалить
                    }
                }
            })
        }
        if (list.isEmpty()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ctx.resources.getString(R.string.vaccination_activity_empty_list), //Добавьте новую вакцинацию, нажав на кнопку снизу справа
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = mainTextColor
                )
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn() {
                    itemsIndexed(list) { index, item ->
                        Card(onClick = {
                            dialogDeleteVaccine.value = true; idOfVaccinationCard =
                            list[index].id; idOfColumn = index
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp, top = 5.dp),
                            elevation = 5.dp,
                            backgroundColor = mainSecondColor) {
                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                Text(
                                    text = list[index].name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                    color = mainTextColor
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 5.dp)) {
                                    Column() {
                                        Text(
                                            text = ctx.resources.getString(R.string.vaccination_activity_drug_name), //Название лекарства
                                            color = Color.Gray
                                        )
                                        Text(
                                            list[index].drugName,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = mainTextColor
                                        )
                                    }
                                    Column(modifier = Modifier.padding(end = 10.dp)) {
                                        Text(
                                            text = ctx.resources.getString(R.string.vaccination_activity_date_vaccination),
                                            color = Color.Gray
                                        ) //дата вакцинации
                                        Text(
                                            dateFormatter(list[index].dateOfVaccinations),
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = mainTextColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
fun dateFormatter(time: Long): String {
//        GlobalScope.launch(Dispatchers.IO) {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
//        }
    return sdf.format(calendar.time)
}