package com.sagirov.ilovedog.Screens.ArticleScreen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.presentation.selectedCard
import com.sagirov.ilovedog.domain.utils.theme.mainBackgroundColor
import com.sagirov.ilovedog.domain.utils.theme.mainSecondColor
import com.sagirov.ilovedog.domain.utils.theme.mainTextColor
import de.charlex.compose.HtmlText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleScreen() {
    val ctx = LocalContext.current
    var selectedArticle: Int = 0
    val feedingArticles = mutableListOf(
        "dog_feeding",
        "how_many_times_a_day_should_a_dog_eat",
        "what_human_foods_are_bad_for_dogs",
        "should_i_feed_my_dog_wet_dog_food",
        "how_to_choose_the_best_small_breed_dog_food"
    )
    val activityArticles = mutableListOf(
        "six_winters_safety_tips_for_dogs_with_active_lifecycles",
        "five_games_to_play_with_your_dog",
        "dog_exercise",
        "running_with_you_dog_getting_started",
        "hiking_with_your_dogs",
        "canine_fitness_for_senior_dogs"
    )
    val ss = (ctx as Activity).intent.getStringExtra("Article").toString()
    var currentArticle = mutableListOf("")
    when (ss) {
        "feeding" -> currentArticle = feedingArticles.toMutableList()
        "activity" -> currentArticle = activityArticles.toMutableList()
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    if (selectedCard.value == -1) {
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
        ) {
            LazyColumn {
                items(currentArticle) {
                    val stringID = ctx.resources.getIdentifier(it, "string", ctx.packageName)
                    val stringIDtext =
                        ctx.resources.getIdentifier(it + "_text", "string", ctx.packageName)
                    selectedArticle = stringIDtext
                    Card(
                        onClick = { selectedCard.value = 2 }, modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(mainSecondColor),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = ctx.resources.getString(stringID),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(start = 15.dp, end = 15.dp, top = 15.dp)
        ) {
            HtmlText(textId = selectedArticle, fontSize = 15.sp, color = mainTextColor)
        }
    }
}