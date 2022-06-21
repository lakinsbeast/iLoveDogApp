package com.sagirov.ilovedog

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        val frst_lnch = prefs.getBoolean("firstOpen", true)
        if (frst_lnch) {
            startActivity(Intent(this,FirstLaunchActivity::class.java))
        }
        setContent {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(0.5f)) {
                        Text(text = "Home", fontWeight = FontWeight.Bold, fontSize = 36.sp)
                    }
                }
                Dashboard()
                Stats()
                Column(Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    OutlinedButton(onClick = { startActivity(Intent(this@MainActivity, WalkLaunchActivity::class.java))},
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
                        Text("Walks!")
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dashboard() {
    Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Card(onClick = { /*TODO*/ }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Row(Modifier.padding(top = 10.dp, bottom = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.dog_first), contentDescription ="", contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(75.dp)
                        .clip(RoundedCornerShape(100)))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "American Pit Buff Terrier")
                    Text(text = "Milou")
                    Text(text = "6 months old")
                }
            }
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Happy", color = Color.Blue)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Stats() {
    Text(text = "Stats", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Card(onClick = { /*TODO*/ }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 30.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Column() {
                Text(text = "Today's plan", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "10% accomplished", color = Color.Gray)
            }
            CircularProgressIndicator(progress = 10F, color = Color.Yellow, strokeWidth = 5.dp)
//            Canvas(modifier = Modifier.size(50.dp), onDraw = {
//                drawCircle(color = Color.Red)
//                drawCircle(radius = 50F, color = Color.Blue)
//            })
        }
    }
        Card(onClick = { /*TODO*/ }) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                Column() {
                    Text(text = "Energy available",fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "90% energy", color = Color.Gray)
                }
                Canvas(modifier = Modifier.size(50.dp), onDraw = {
                    drawCircle(color = Color.Red)
                    drawCircle(radius = 50F, color = Color.Blue)
                })
            }
        }
    Card(onClick = { /*TODO*/ }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 30.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Column() {
                Text(text = "Weekly objectives",fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "2 walks left", color = Color.Gray)
            }
            Canvas(modifier = Modifier.size(50.dp), onDraw = {
                drawCircle(color = Color.Red)
                drawCircle(radius = 50F, color = Color.Blue)
            })
        }
    }
}

