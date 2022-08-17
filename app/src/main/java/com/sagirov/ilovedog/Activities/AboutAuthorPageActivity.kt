package com.sagirov.ilovedog.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAuthorPageActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    elevation = 0.dp, modifier = Modifier
                        .fillMaxWidth(), backgroundColor = mainBackgroundColor
                ) {
                    Column() {
                        Text(
                            "Это приложение сделано студентом-самоучкой, я занимаюсь андроид-разаботкой в свободное время" +
                                    " и мне это очень нравится, приложение я делал с любовью и желанием, планирую в дальнейшем его развивать на благо" +
                                    " собачникам", color = mainTextColor
                        )
                        Text(
                            "Приложение может быть пустоватым или чего-то может не хватать, я не собачник (пока что), поэтому особо" +
                                    " не знаю потребностей собачников, всегда буду рад выслушать любые замечания и любые пожелания, обязательно" +
                                    " на каждый вопрос, пожелание или просто доброе слово - я отвечу, обещаю",
                            color = mainTextColor
                        )
                        Text(
                            "Как разработчик скажу, что тяжело стараться делать хорошее приложение, понимая, что монетизации в приложении не будет" +
                                    ", поэтому если есть желание, то можете задонатить копеечку автору, которая даст немного мотивации продолжать " +
                                    "работать над продуктом, а если получится получать небольшой доход, то получится уйти с работы и уделять очень много " +
                                    "времени приложению", color = mainTextColor
                        )
                    }
                }
                //TODO{Копируется в буфер обмена несколько раз, если нажать несколько раз}
                val copy = LocalClipboardManager.current
                Card(
                    onClick = {
                        copy.setText(AnnotatedString("phyqstudio@gmail.com"));
                        Toast.makeText(
                            this@AboutAuthorPageActivity, "Скопировано в буфер обмена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }, elevation = 10.dp, modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(top = 20.dp), backgroundColor = mainBackgroundColor
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "",
                            tint = mainTextColor
                        )
                        Text(
                            "phyqstudio@gmail.com",
                            color = mainTextColor,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                Card(
                    onClick = {
                        copy.setText(AnnotatedString("5228 6005 7978 4499"));
                        Toast.makeText(
                            this@AboutAuthorPageActivity, "Скопировано в буфер обмена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }, elevation = 10.dp, modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(top = 20.dp), backgroundColor = mainBackgroundColor
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.credit_card_48px),
                            contentDescription = "", tint = mainTextColor
                        )
                        Text(
                            "5228 6005 7978 4499 - Сбербанк",
                            color = mainTextColor,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
