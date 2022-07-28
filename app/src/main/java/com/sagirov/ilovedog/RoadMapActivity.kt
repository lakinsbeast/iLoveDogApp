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
                    Text(text = "Изменение название документа/фото в папке 'Документы'")
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Профиль нескольких собак")
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Наполнить вкладку 'Настройки' в меню")
                }
                Card(modifier = Modifier
                    .fillMaxWidth().background(mainSecondColor)
                    .height(50.dp)) {
                    Text(text = "Добавить достижения")
                }
            }
        }
    }
}
