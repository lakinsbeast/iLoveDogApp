package com.sagirov.ilovedog.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstLaunchActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        setContent {
            Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .weight(0.5f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "iLoveDog!", fontSize = 56.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }
                    }
                }
                Box(
                    Modifier
                        .weight(2f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(painterResource(id = R.drawable.painter_girl_with_dog), contentDescription = "here is no content desc",
                        contentScale = ContentScale.Fit)
                    }
                }
                Box(
                    Modifier
                        .weight(0.5f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Это приложение было создано для заботы за братьями наши меньшими на основе их потребностей." +
                                    "Следите за их прогрессом и здоровьем!", fontSize = 18.sp, color = Color.Black, textAlign = TextAlign.Center)
                        }
                    }
                }
                Box(
                    Modifier
                        .weight(0.5f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        OutlinedButton(onClick = {  /*edit.putBoolean("firstOpen", false).apply()*/
                            startActivity(Intent(applicationContext, NewPetActivity::class.java)); finish() },
                            Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp
                                )
                                .height(50.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp, color = Color.Black,
                                    shape = RoundedCornerShape(50)
                                )
                                .clip(RoundedCornerShape(50)),
                            colors = ButtonDefaults.buttonColors(backgroundColor = mainSecondColor, contentColor = Color.Black)) {
                            Text("Let's start!")
                        }
                    }
                }
            }
//            Text(text = "THIS IS LAUNCH ACTIVITY", fontSize = 36.sp)
        }
    }
}

@Composable
fun titleText() {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Dog Tracking")
    }
}