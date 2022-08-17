package com.sagirov.ilovedog.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstLaunchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .weight(0.5f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "iLoveDog!",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Medium,
                                color = mainTextColor
                            )
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
                            Text(
                                text = "Это приложение было создано для заботы за братьями наши меньшими на основе их потребностей." +
                                        "Следите за их прогрессом и здоровьем!",
                                fontSize = 18.sp,
                                color = mainTextColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Box(
                    Modifier
                        .weight(0.5f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        OutlinedButton(
                            onClick = {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        NewPetActivity::class.java
                                    )
                                ); finish()
                            },
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
                            Text("Начать", color = mainTextColor)
                        }
                    }
                }
            }
        }
    }
}
