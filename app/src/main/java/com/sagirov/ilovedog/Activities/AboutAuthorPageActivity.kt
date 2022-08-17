package com.sagirov.ilovedog.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@AndroidEntryPoint
class AboutAuthorPageActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(elevation = 0.dp, modifier = Modifier
                    .fillMaxWidth()) {
                    Column(modifier = Modifier.background(mainBackgroundColor)) {
                        Text("Это приложение сделано студентом-самоучкой, я занимаюсь андроид-разаботкой в свободное время" +
                                " и мне это очень нравится, приложение я делал с любовью и желанием, планирую в дальнейшем его развивать на благо" +
                                " собачникам")
                        Text("Приложение может быть пустоватым или чего-то может не хватать, я не собачник (пока что), поэтому особо" +
                                " не знаю потребностей собачников, всегда буду рад выслушать любые замечания и любые пожелания, обязательно" +
                                " на каждый вопрос, пожелание или просто доброе слово - я отвечу, обещаю")
                        Text("Как разработчик скажу, что тяжело стараться делать хорошее приложение, понимая, что монетизации в приложении не будет" +
                                ", поэтому если есть желание, то можете задонатить копеечку автору, которая даст немного мотивации продолжать " +
                                "работать над продуктом, а если получится получать небольшой доход, то получится уйти с работы и уделять очень много " +
                                "времени приложению")
                    }
                }
                //TODO{Копируется в буфер обмена несколько раз, если нажать несколько раз}
                val copy = LocalClipboardManager.current
                Card(onClick = { copy.setText(AnnotatedString("phyqstudio@gmail.com"));
                               Toast.makeText(this@AboutAuthorPageActivity, "Скопировано в буфер обмена",
                                   Toast.LENGTH_SHORT).show()}, elevation = 10.dp, modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 20.dp)) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .background(mainBackgroundColor), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Email, contentDescription = "")
                            Text("phyqstudio@gmail.com")
                        }
                }
                Card(onClick = { copy.setText(AnnotatedString("5228 6005 7978 4499"));
                    Toast.makeText(this@AboutAuthorPageActivity, "Скопировано в буфер обмена",
                        Toast.LENGTH_SHORT).show()}, elevation = 10.dp, modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 20.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.credit_card_48px), contentDescription = "")
                        Text("5228 6005 7978 4499 - Сбербанк")
                    }
                }
            }
        }
    }
}
