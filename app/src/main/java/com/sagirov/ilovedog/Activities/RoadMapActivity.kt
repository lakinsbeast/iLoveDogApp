package com.sagirov.ilovedog.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoadMapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Изменение название документа/фото в папке 'Документы'",
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        modifier = Modifier.background(mainSecondColor), color = mainTextColor
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .height(50.dp)) {
                    Text(
                        text = "Профиль нескольких собак",
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        modifier = Modifier.background(mainSecondColor),
                        color = mainTextColor
                    )
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .height(50.dp)) {
                    Text(
                        text = "Наполнить вкладку 'Настройки' в меню",
                        modifier = Modifier.background(
                            mainSecondColor
                        ),
                        color = mainTextColor
                    )
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .height(50.dp)) {
                    Text(
                        text = "Добавить достижения", modifier = Modifier.background(
                            mainSecondColor
                        ), color = mainTextColor
                    )
                }
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .height(50.dp)) {
                    Text(
                        text = "Если в напоминалках стоит 31 число, то ставится 32 число, которые не может быть :/",
                        modifier = Modifier.background(
                            mainSecondColor
                        ),
                        color = mainTextColor
                    )
                }

            }
        }
    }
}
