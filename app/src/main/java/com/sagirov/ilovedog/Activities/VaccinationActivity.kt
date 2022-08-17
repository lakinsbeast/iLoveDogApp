package com.sagirov.ilovedog.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.VaccinationsEntity
import com.sagirov.ilovedog.ViewModels.VaccinationViewModel
import com.sagirov.ilovedog.ViewModels.VaccinationViewModelFactory
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*

@ExperimentalMaterialApi
@AndroidEntryPoint
class VaccinationActivity : ComponentActivity() {
    private var vaccinationList = mutableStateListOf<VaccinationsEntity>()
    private val vaccineViewModel: VaccinationViewModel by viewModels {
        VaccinationViewModelFactory((application as DogsApplication).VaccinationAppRepo)
    }
    private var idOfColumn = 0

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vaccineViewModel.getAllVaccinations.observe(this) {
            vaccinationList.clear()
            vaccinationList.addAll(it)
            vaccinationList.forEach {
                Log.d("list", it.toString())
            }
        }

        setContent {
            val dialogDeleteVaccine = remember { mutableStateOf(false)}
            Scaffold(topBar = {}, floatingActionButton = {
                FloatingActionButton(onClick = { startActivity(Intent(this@VaccinationActivity,
                    NewVaccineActivity::class.java))}, content = {
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
                                    vaccineViewModel.deleteVaccination(idOfColumn)
                                    idOfColumn = 0
                                    dialogDeleteVaccine.value = false
                                },
                                Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                                ,shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = mainSecondColor,
                                    contentColor = Color.Black
                                ), contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(text = "Удалить")
                            }
                        }
                    })
                }
                if (vaccinationList.isEmpty()) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Добавьте новую вакцинацию, нажав на кнопку снизу справа",
                            fontWeight = FontWeight.Bold, fontSize = 40.sp, textAlign = TextAlign.Center)
                    }
                } else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        LazyColumn() {
                            itemsIndexed(vaccinationList) { index, item ->
                                Card(onClick = {dialogDeleteVaccine.value = true; idOfColumn = vaccinationList[index].id} ,modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp, top = 5.dp), elevation = 5.dp
                                    , backgroundColor = mainSecondColor) {
                                    Column(modifier = Modifier.padding(start = 10.dp)) {
                                        Text(text = vaccinationList[index].name, fontWeight = FontWeight.SemiBold, fontSize = 20.sp,
                                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 5.dp)) {
                                            Column() {
                                                Text(text = "Название лекарства", color = Color.Gray)
                                                Text(vaccinationList[index].drugName, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                                            }
                                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                                Text(text = "Дата вакцинации", color = Color.Gray)
                                                Text(dateFormatter(vaccinationList[index].dateOfVaccinations),
                                                    fontSize = 17.sp, fontWeight = FontWeight.Medium)
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
    }
    fun dateFormatter(time: Long): String {
//        GlobalScope.launch(Dispatchers.IO) {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
//        }
        return sdf.format(calendar.time)
    }
}
