package com.sagirov.ilovedog.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.ViewModels.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.ViewModels.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.ui.theme.encyclopediaDogBarColor
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@AndroidEntryPoint
class DetailedDogActivity : ComponentActivity() {
    val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()

    private val dogsBreedViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).DogsBreedEncyclopediaAppRepo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var resImage = -1
        var id = intent.getIntExtra("id", 0)
        dogsBreedViewModel.allDogs.observe(this) { list ->
            dogsEncyclopedia.add(
                DogsBreedEncyclopediaEntity(list[id].id, list[id].breedName, list[id].origin, list[id].breedGroup, list[id].lifeSpan,
                    list[id].type, list[id].temperament, list[id].male_height, list[id].female_height, list[id].male_weight,
                    list[id].female_weight, list[id].colors, list[id].litterSize, list[id].description, list[id].imageFile)
            )
            if (list[id].imageFile != "") {
                resImage = resources.getIdentifier(list[id].imageFile,"drawable", packageName)
            } else {
                resImage = -578358347
            }
            setContent {
                Column(
                    Modifier
                        .fillMaxSize().background(mainBackgroundColor)
                        .verticalScroll(rememberScrollState(), enabled = true)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                        Text(dogsEncyclopedia[0].breedName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        if (resImage != -578358347){
                            Image(
                                painterResource(resImage), contentDescription ="", contentScale = ContentScale.Fit)
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                        Text("Происхождение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(text = "Место", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].origin, Modifier.padding(start = 41.dp))
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                        Text("Характеристики", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(text = "Рост", fontWeight = FontWeight.Bold)
                        Column(Modifier.padding(start = 50.dp)) {
                           Text(text = "Кобели")
                           Text(text = "Суки")
                        }
                        Column(Modifier.padding(start = 50.dp)) {
                            Text(text = dogsEncyclopedia[0].male_height, Modifier.padding(start = 50.dp))
                            Text(text = dogsEncyclopedia[0].female_height, Modifier.padding(start = 50.dp))
                        }

                    }
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(text = "Масса", fontWeight = FontWeight.Bold)
                        Column(Modifier.padding(start = 40.dp, top = 5.dp)) {
                            Text(text = "Кобели")
                            Text(text = "Суки")
                        }
                        Column(Modifier.padding(start = 50.dp, top = 5.dp)) {
                            Text(text = dogsEncyclopedia[0].male_weight, Modifier.padding(start = 50.dp))
                            Text(text = dogsEncyclopedia[0].female_weight, Modifier.padding(start = 50.dp))
                        }
                    }
                    Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                        Text(text = "Цвета", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].colors, Modifier.padding(start = 40.dp))
                    }
                    Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                        Text(text = "Срок \nжизни", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].lifeSpan, Modifier.padding(start = 37.dp))
                    }
                    Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                        Text(text = "Тип", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].type, Modifier.padding(start = 57.dp))
                    }
                    Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                        Text(text = "Поводок", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].litterSize, Modifier.padding(start = 25.dp))
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                        Text("Другие классификации", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(text = "Группы", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].breedGroup, Modifier.padding(start = 35.dp))
                    }
                }
            }
        }
    }
}
