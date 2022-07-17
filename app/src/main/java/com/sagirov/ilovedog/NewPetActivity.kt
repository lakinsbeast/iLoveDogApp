package com.sagirov.ilovedog

import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme
import java.util.*

class NewPetActivity : ComponentActivity() {
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences


    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val edit = prefsMyPet.edit()
        setContent {
            var petNameBreed = rememberSaveable { mutableStateOf("")}
            var petName = rememberSaveable { mutableStateOf("")}
            var petAge = rememberSaveable { mutableStateOf("")}
            var petAgeMonth = rememberSaveable { mutableStateOf("")}
            var petPaddock = rememberSaveable { mutableStateOf("")}
            val screenWidth = LocalConfiguration.current.screenWidthDp
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(label = { Text(text = "Имя питомца:", fontSize = 15.sp)}, value = petName.value, onValueChange = {petName.value = it},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                Row() {
                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                        value = petAge.value, onValueChange = {petAge.value = it}, label = {Text("Лет")}, modifier = Modifier.width((screenWidth / 2.79).dp))
                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                        value = petAgeMonth.value, onValueChange = {petAgeMonth.value = it}, label = {Text("Месяцев")}, modifier = Modifier.width((screenWidth / 2.79).dp))
                }
                TextField(placeholder = { Text(text = "Порода питомца:", fontSize = 15.sp)},value = petNameBreed.value, onValueChange = {petNameBreed.value = it},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                TextField(placeholder = { Text(text = "Время выгула питомца?(минут):", fontSize = 15.sp)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent), value = petPaddock.value, onValueChange = {petPaddock.value = it})
                OutlinedButton(onClick = {
                    if ((petAge.value!= "" && petName.value != "" && petNameBreed.value != "" && petPaddock.value != "")) {
                        petPaddock.value.toLong()
                        petPaddock.value = (petPaddock.value.toLong()*60000).toString()
                        edit.putString("mypetName", petName.value)
                        edit.putString("mypetBreed", petNameBreed.value)
                        edit.putString("mypetAge", petAge.value)
                        edit.putString("mypetAgeMonth", petAgeMonth.value)
                        edit.putString("mypetPaddock", petPaddock.value)
                        edit.putString("mypetPaddockStandart", petPaddock.value)
                        edit.apply()

                        dogsViewModel.insertDogProfile(DogsInfoEntity(0,petName.value,Calendar.getInstance().time,petPaddock.value.toLong(),Calendar.getInstance().time, petNameBreed.value,"Сука",petPaddock.value.toLong(), 56,"fuck"))

                        startActivity(Intent(this@NewPetActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@NewPetActivity, "Заполните все поля", Toast.LENGTH_LONG).show()
                    } },
                    Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp, top = 20.dp
                        )
                        .height(70.dp)
                        .fillMaxWidth()
                        .border(
                            width = 0.dp, color = Color.Black,
                            shape = RoundedCornerShape(50)
                        )
                        .clip(RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                    Text("Добавить питомца")
                }

            }


        }
    }
}
