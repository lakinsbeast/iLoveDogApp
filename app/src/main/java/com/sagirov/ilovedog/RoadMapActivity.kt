package com.sagirov.ilovedog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme
import com.sagirov.ilovedog.ui.theme.mainSecondColor

class RoadMapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Изменение название документа/фото в папке 'Документы'",
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        modifier = Modifier.background(mainSecondColor))
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Профиль нескольких собак", style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        modifier = Modifier.background(mainSecondColor))
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Наполнить вкладку 'Настройки' в меню", modifier = Modifier.background(
                        mainSecondColor))
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Добавить достижения", modifier = Modifier.background(
                        mainSecondColor))
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Если в напоминалках стоит 31 число, то ставится 32 число, которые не может быть :/", modifier = Modifier.background(
                        mainSecondColor))
                }

            }
        }
    }
}
