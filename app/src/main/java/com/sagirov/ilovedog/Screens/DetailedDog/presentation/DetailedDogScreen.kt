package com.sagirov.ilovedog.Screens.DetailedDog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.Screens.DetailedDog.presentation.DogsEncyclopediaViewModel
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.ui.theme.encyclopediaDogBarColor
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainTextColor
import kotlinx.coroutines.launch

@Composable
fun DetailedDogScreen(
    navController: NavController,
    id: Int,
    viewModel: DogsEncyclopediaViewModel = hiltViewModel()
) {
    val dogsEncyclopedia = remember { mutableStateListOf<DogsBreedEncyclopediaEntity>() }

    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(rememberCoroutineScope()) {
        lifecycleOwner.lifecycleScope.launch {
            dogsEncyclopedia.clear()
            val dog = viewModel.getDogById(id)
            dogsEncyclopedia.add(dog)
        }
    }
//    LaunchedEffect(rememberCoroutineScope()) {
//        lifecycleOwner.lifecycleScope.launch {
//
//            viewModel.allDogs.collect() { list ->
//                dogsEncyclopedia.clear()
//                list.forEach {
//                    dogsEncyclopedia.add(
//                        DogsBreedEncyclopediaEntity(
//                            list[id].id,
//                            list[id].breedName,
//                            list[id].origin,
//                            list[id].breedGroup,
//                            list[id].lifeSpan,
//                            list[id].type,
//                            list[id].temperament,
//                            list[id].male_height,
//                            list[id].female_height,
//                            list[id].male_weight,
//                            list[id].female_weight,
//                            list[id].colors,
//                            list[id].litterSize,
//                            list[id].description,
//                            list[id].imageFile
//                        )
//                    )
//                } }
//        }
//    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    Column(
        Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
            .verticalScroll(rememberScrollState(), enabled = true)
    ) {
        if (dogsEncyclopedia.isNotEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(encyclopediaDogBarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    dogsEncyclopedia[0].breedName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
                )
            }
            if (dogsEncyclopedia[0].imageFile != "") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painterResource(
                            ctx.resources.getIdentifier(
                                dogsEncyclopedia[0].imageFile,
                                "drawable",
                                ctx.packageName
                            )
                        ), contentDescription = "", contentScale = ContentScale.Fit
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                Text(
                    "Происхождение",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(start = 10.dp)) {
                Text(text = "Место", fontWeight = FontWeight.Bold, color = mainTextColor)
                Text(
                    text = dogsEncyclopedia[0].origin,
                    Modifier.padding(start = 41.dp),
                    color = mainTextColor
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                Text(
                    "Характеристики",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(start = 10.dp)) {
                Text(text = "Рост", fontWeight = FontWeight.Bold, color = mainTextColor)
                Column(Modifier.padding(start = 50.dp)) {
                    Text(text = "Кобели", color = mainTextColor)
                    Text(text = "Суки", color = mainTextColor)
                }
                Column(Modifier.padding(start = 50.dp)) {
                    Text(
                        text = dogsEncyclopedia[0].male_height,
                        Modifier.padding(start = 50.dp),
                        color = mainTextColor
                    )
                    Text(
                        text = dogsEncyclopedia[0].female_height,
                        Modifier.padding(start = 50.dp),
                        color = mainTextColor
                    )
                }

            }
            Row(Modifier.padding(start = 10.dp)) {
                Text(text = "Масса", fontWeight = FontWeight.Bold, color = mainTextColor)
                Column(Modifier.padding(start = 40.dp, top = 5.dp)) {
                    Text(text = "Кобели", color = mainTextColor)
                    Text(text = "Суки", color = mainTextColor)
                }
                Column(Modifier.padding(start = 50.dp, top = 5.dp)) {
                    Text(
                        text = dogsEncyclopedia[0].male_weight,
                        Modifier.padding(start = 50.dp),
                        color = mainTextColor
                    )
                    Text(
                        text = dogsEncyclopedia[0].female_weight,
                        Modifier.padding(start = 50.dp),
                        color = mainTextColor
                    )
                }
            }
            Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                Text(text = "Цвета", fontWeight = FontWeight.Bold, color = mainTextColor)
                Text(
                    text = dogsEncyclopedia[0].colors,
                    Modifier.padding(start = 40.dp),
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                Text(
                    text = "Срок \nжизни",
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
                )
                Text(
                    text = dogsEncyclopedia[0].lifeSpan,
                    Modifier.padding(start = 37.dp),
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                Text(text = "Тип", fontWeight = FontWeight.Bold, color = mainTextColor)
                Text(
                    text = dogsEncyclopedia[0].type,
                    Modifier.padding(start = 57.dp),
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(top = 5.dp, start = 10.dp)) {
                Text(text = "Поводок", fontWeight = FontWeight.Bold, color = mainTextColor)
                Text(
                    text = dogsEncyclopedia[0].litterSize,
                    Modifier.padding(start = 25.dp),
                    color = mainTextColor
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(encyclopediaDogBarColor), contentAlignment = Alignment.Center) {
                Text(
                    "Другие классификации",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor
                )
            }
            Row(Modifier.padding(start = 10.dp)) {
                Text(text = "Группы", fontWeight = FontWeight.Bold, color = mainTextColor)
                Text(
                    text = dogsEncyclopedia[0].breedGroup,
                    Modifier.padding(start = 35.dp),
                    color = mainTextColor
                )
            }
        }
    }
}