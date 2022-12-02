package com.sagirov.ilovedog.Screens.DogNutritionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.domain.utils.theme.homeButtonColor
import com.sagirov.ilovedog.domain.utils.theme.mainBackgroundColor
import com.sagirov.ilovedog.domain.utils.theme.mainSecondColor
import com.sagirov.ilovedog.domain.utils.theme.mainTextColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DogNutritionScreen(navController: NavController) {
    val titlesRu =
        listOf("Примечание", "Мясо", "Кости", "Фрукты и овощи", "Запрещенные продукты!")
    val titlesEn =
        listOf("Примечание", "Мясо", "Кости", "Фрукты и овощи", "Запрещенные продукты!")
    val meatRu = listOf(
        "Говядина",
        "Телятина",
        "Мерлушка",
        "Печень",
        "Курятина и птица",
        "Дичина",
        "Рыба"
    )
    val boneRu = listOf(
        "Сырые крылышки",
        "Шейки",
        "Спинка курицы",
        "Крылья и шейки индюшки",
        "Разнообразные птичьи субпродукты",
        "Говяжья грудинка",
        "Ребрышки",
        "Хвосты",
        "Рыба"
    )
    val fruitsAndVegetablesRu = listOf(
        "Огурцы",
        "Кольраби",
        "Морковь",
        "Банан",
        "Груша",
        "Персик",
        "Клубника",
        "Капуста",
        "Кукуруза",
        "Салат",
        "Рукола"
    )
    val illegalProductsRu = listOf(
        "Хлебобулочные изделия",
        "Сладости",
        "Макароны",
        "Жирное мясо - нежелательно",
        "Соленья и консервация",
        "Грибы",
        "Жареное",
        "Кукураз и крупа",
        "Соевые продукть",
        "Винограды",
        "Чеснок, лук",
        "Специи",
        "Соль",
        "Кофе, чай",
        "Зеленые томаты",
        "Авокадо"
    )
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(mainBackgroundColor)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Column {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = mainBackgroundColor,
            contentColor = Color.Black,
            indicator = {
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions = it
                    )
                )
            }) {
            titlesRu.forEachIndexed { index, s ->
                Tab(
                    text = { Text(s, color = mainTextColor) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.scrollToPage(index) } })
            }
        }
//                if (tabRowState.value == 1) {
//                ScrollableTabRow(selectedTabIndex = tabRowSecondState.value) {
//                    meatTitles.forEachIndexed { index, s ->
//                        Tab(text = { Text(s) }, selected = tabRowSecondState.value == index, onClick = { tabRowSecondState.value = index })
//                    }
//                }
//                }
        HorizontalPager(
            count = 5, state = pagerState, modifier = Modifier
                .fillMaxSize()
                .background(
                    homeButtonColor
                )
        ) {
            when (it) {
                0 -> {
                    TitleText()
                }
                1 -> MeatTitle(list = meatRu)
                2 -> MeatTitle(list = boneRu)
                3 -> MeatTitle(list = fruitsAndVegetablesRu)
                4 -> MeatTitle(list = illegalProductsRu)
            }
        }
//                when (tabRowState.value) {
//                    0 -> { TitleText() }
//                    1 -> MeatTitle(list = meat)
//                    2 -> MeatTitle(list = bone)
//                    3 -> MeatTitle(list = fruitsAndVegetables)
//                    4 -> MeatTitle(list = illegalProducts)
//                }
    }
}

@Composable
fun TitleText() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        Text(
            "Диета B.A.R.F означает две общие фразы: \"Биологически подходящая сырая пища\" и \"Кости и сырая пища\". Принцип диеты, основанной ветеринаром и диетологом доктором Яном Биллингхерстом, заключается в том, чтобы кормить собак тем рационом, на котором они эволюционировали - сырой пищей, состоящей из свежего, неприготовленного и дикого мяса и зелени. \n",
            fontSize = 20.sp,
            color = mainTextColor
        )
        Text(
            "По мнению основателя системы кормления для животных БАРФ, собаки – это хищники, поэтому основой их питания должен являться природный рацион диких предков. Такая пища является полезной и здоровой для животных. А что касается сырых продуктов, по мнению автора этой методики кормления, пища после термической обработки утрачивает ряд питательных веществ, необходимых для организма, поэтому является бесполезной для животного. \n",
            fontSize = 20.sp,
            color = mainTextColor
        )
        Text(
            "Приучать собаку к питанию по системе BARF нужно постепенно, соблюдая некоторые рекомендации создателя методики. Чтобы организм привык к такой пище, первое время за 30 минут до еды нужно давать собаке пребиотик и бифидобактерии, нормализующие кишечную микрофлору. Каждый новый продукт вводится постепенно, в небольшом количестве. Как только собака полностью перейдет на новый рацион, можно исключать лекарственные компоненты, так как они уже будут не нужны здоровому организму. ",
            fontSize = 20.sp,
            color = mainTextColor
        )

    }
}

@Composable
fun MeatTitle(list: List<String>) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            list.forEach {
                item {
                    Card(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(0)) {
                        Row(
                            modifier = Modifier
//                        .fillMaxSize()
                                .background(mainSecondColor)
                                .padding(top = 20.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(it, fontSize = 20.sp, color = mainTextColor)
                        }
                    }
                }
            }
        }
    }

}