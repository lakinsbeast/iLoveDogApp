package com.sagirov.ilovedog

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val selectedIndex =  mutableStateOf(0)
class MainActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"
    private lateinit var prefs: SharedPreferences


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        val frst_lnch = prefs.getBoolean("firstOpen", true)
        if (frst_lnch) {
            startActivity(Intent(this,FirstLaunchActivity::class.java))
        }




        // add alarm managerss

//        val am = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        am.setExact(AlarmManager.RTC_WAKEUP, 1000 * 10, pendingIntent)


        setContent {
            Scaffold(bottomBar = { BottomNav()}) {
                if (selectedIndex.value == 0) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState(), true)
                            .fillMaxSize()
                            .padding(start = 15.dp, end = 15.dp, bottom = 50.dp)) {
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
                            OutlinedButton(onClick = { startActivity(Intent(this@MainActivity, ReminderActivity::class.java))},
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
                                Text("Добавить напоминание к походу к ветеринару")
                            }
                        }
                    }
                }
                if (selectedIndex.value == 1) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 15.dp, end = 15.dp)) {
                        Row(Modifier.fillMaxWidth()) {
                            Box(Modifier.weight(0.5f)) {
                                Text(text = "My pets", fontWeight = FontWeight.Bold, fontSize = 36.sp)
                            }
                        }
                        DashboardPets()
                    }
                }

            }

        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Dashboard() {
        Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card(onClick =  {
            selectedIndex.value = 1
//            startActivity(Intent(this@MainActivity, MyPetsActivity::class.java))
        }) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardPets() {
    Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Card(onClick = { /*TODO*/ }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
            Row(Modifier.padding(top = 10.dp, bottom = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.dog_first), contentDescription ="", contentScale = ContentScale.Crop,
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

@Composable
fun BottomNav() {
    BottomNavigation(backgroundColor = Color.White) {
        var home = Icons.Outlined.Home
        var myPets = R.drawable.pets_48px
        var myCard = R.drawable.ic_launcher_background
        var profile = Icons.Outlined.Person
        BottomNavigationItem(icon = {
            Icon(imageVector = home,"")
        },
//            label = { Text(text = "Home") },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = ImageVector.vectorResource(myPets),"", modifier = Modifier.size(25.dp))
        },
//            label = { Text(text = "Statistic") },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
            })
        BottomNavigationItem(icon = {
            Icon(imageVector = ImageVector.vectorResource(myCard),"", modifier = Modifier.size(25.dp))
        },
//            label = { Text(text = "My Card") },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
            })
        BottomNavigationItem(icon = {
            Icon(imageVector = profile,"", modifier = Modifier.size(25.dp))
        },
//            label = { Text(text = "Profile") },
            selected = (selectedIndex.value == 3),
            onClick = {
                selectedIndex.value = 3
            })
    }
}