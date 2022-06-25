package com.sagirov.ilovedog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MyPetsActivity : ComponentActivity() {
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val myPetName = prefsMyPet.getString("mypetName", "")
        val myPetNameBreed = prefsMyPet.getString("mypetBreed", "")
        val myPetAge = prefsMyPet.getInt("mypetAge", 0)
        val myPetPaddock = prefsMyPet.getInt("mypetPaddock", 0)


        val edit = prefsMyPet.edit()
        setContent {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(0.5f)) {
                        Text(text = "My pets", fontWeight = FontWeight.Bold, fontSize = 36.sp)
                    }
                }
                DashboardPets(myPetName.toString(),
                    myPetNameBreed.toString(),myPetAge.toString(),myPetPaddock.toString())
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardPets(name: String, breed: String, age: String, paddock: String) {
    Text(text = "My dogs", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Card {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.padding(top = 10.dp, bottom = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.dog_first), contentDescription ="", contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(75.dp)
                        .clip(RoundedCornerShape(100)))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = breed)
                    Text(text = name)
                    Text(text = "$age лет")
                }
            }
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(paddock, color = Color.Blue)
            }
        }
    }
}
}