package com.sagirov.ilovedog

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme

class NewPetActivity : ComponentActivity() {
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val edit = prefsMyPet.edit()
        setContent {
            var petNameBreed = rememberSaveable { mutableStateOf("")}
            var petName = rememberSaveable { mutableStateOf("")}
            var petAge = rememberSaveable { mutableStateOf("")}
            var petPaddock = rememberSaveable { mutableStateOf("")}
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                Text("Имя питомца:")
                TextField(label = { Text(text = "Имя питомца:", fontSize = 15.sp)}, value = petName.value, onValueChange = {petName.value = it})
//                Text("Возраст питомца:")
                TextField(placeholder = { Text(text = "Возраст питомца:", fontSize = 15.sp)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = petAge.value, onValueChange = {petAge.value = it})
//                Text("Порода питомца:")
                TextField(placeholder = { Text(text = "Порода питомца:", fontSize = 15.sp)},value = petNameBreed.value, onValueChange = {petNameBreed.value = it})
//                Text("Сколько нужно выгуливать питомца(в минутах)?:")
                TextField(placeholder = { Text(text = "Время выгула питомца?(минут):", fontSize = 15.sp)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = petPaddock.value, onValueChange = {petPaddock.value = it})
                OutlinedButton(onClick = {
                    if ((petAge.value!= "" && petName.value != "" && petNameBreed.value != "" && petPaddock.value != "")) {
                        edit.putString("mypetName", petName.value)
                        edit.putString("mypetBreed", petNameBreed.value)
                        edit.putString("mypetAge", petAge.value)
                        edit.putString("mypetPaddock", petPaddock.value)
                        edit.apply()
                        startActivity(Intent(this@NewPetActivity, MainActivity::class.java))

                    } else {
                        Toast.makeText(this@NewPetActivity, "Заполните все поля", Toast.LENGTH_LONG).show()
                    } },

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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                    Text("Добавить питомца")
                }

            }


        }
    }
}
