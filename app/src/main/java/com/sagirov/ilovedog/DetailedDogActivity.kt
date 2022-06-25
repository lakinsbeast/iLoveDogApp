package com.sagirov.ilovedog

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme

class DetailedDogActivity : ComponentActivity() {
    val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()

    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var id = intent.getIntExtra("id", 0)
        dogsViewModel.allDogs.observe(this) { list ->
            dogsEncyclopedia.add(
                DogsBreedEncyclopediaEntity(list[id].id, list[id].breedName, list[id].origin, list[id].breedGroup, list[id].lifeSpan,
                    list[id].type, list[id].temperament, list[id].male_height, list[id].female_height, list[id].male_weight,
                    list[id].female_weight, list[id].colors, list[id].litterSize, list[id].description, list[id].imageFile)
            )
            val resImage = resources.getIdentifier(list[id].imageFile,"drawable", packageName)
            setContent {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState(), enabled = true)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Gray), contentAlignment = Alignment.Center) {
                        Text(dogsEncyclopedia[0].breedName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painterResource(resImage), contentDescription ="", contentScale = ContentScale.Fit)
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Gray), contentAlignment = Alignment.Center) {
                        Text("Происхождение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(text = "Место", fontWeight = FontWeight.Bold)
                        Text(text = dogsEncyclopedia[0].origin, Modifier.padding(start = 41.dp))
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Gray), contentAlignment = Alignment.Center) {
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
                            .background(Color.Gray), contentAlignment = Alignment.Center) {
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
