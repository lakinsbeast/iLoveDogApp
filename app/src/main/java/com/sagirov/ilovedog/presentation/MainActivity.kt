package com.sagirov.ilovedog.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.Activities.MainActivity.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.utils.CheckDarkModeManager
import com.sagirov.ilovedog.domain.utils.CheckFirstLaunchManager
import com.sagirov.ilovedog.BottomTabs
import com.sagirov.ilovedog.NavGraphRoutes
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.Screens.AboutAuthorScreen.AboutAuthorScreen
import com.sagirov.ilovedog.Screens.DogNutritionScreen.DogNutritionScreen
import com.sagirov.ilovedog.domain.utils.PreferencesUtils
import com.sagirov.ilovedog.domain.utils.TextUtils
import com.sagirov.ilovedog.domain.utils.analytics.google.GMSImpl
import com.sagirov.ilovedog.domain.utils.analytics.huawei.HMSImpl
import com.sagirov.ilovedog.domain.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.domain.utils.theme.*
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var HMSUtils: HMSImpl

    @Inject
    lateinit var GMSUtils: GMSImpl

    @Inject
    lateinit var newPrefs: PreferencesUtils

    @Inject
    lateinit var darkMode: CheckDarkModeManager

    @Inject
    lateinit var textUtils: TextUtils

    @Inject
    lateinit var checkFirstLaunch: CheckFirstLaunchManager

    companion object {
        var myPetPaddockT = MutableStateFlow(false)
    }

    private var badgeCount = mutableStateOf(0)
    private var isBadgeShown = mutableStateOf(false)


    private val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()
    private val dogsProfileArray = mutableStateListOf<DogsInfoEntity>()
    private val currentDogId = MutableStateFlow(0)
    private var currentTimeById by mutableStateOf(0F)
    private var currentTimeConstById by mutableStateOf(0F)

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HMSUtils = HMSImpl()
        GMSUtils = GMSImpl()
        textUtils = TextUtils()
        checkFirstLaunch = CheckFirstLaunchManager(this)
        newPrefs = PreferencesUtils(this)
        darkMode = CheckDarkModeManager(this)

        if (HMSUtils.checkHuaweiApi(this)) {
            HMSUtils.enableHuaweiAnalytics(this)
        }
        if (GMSUtils.checkGoogleApi(this)) {
            GMSUtils.enableGoogleAnalytics(this)
        }
        darkMode.checkDarkMode()


        checkFirstLaunch.checkFirstLaunch()
//        val frst_lnch = newPrefs.getBoolean(PreferencesUtils.PREF_NAME, "firstOpen", true)
//        if (frst_lnch) {
//            newPrefs.putLong(PreferencesUtils.PREF_SCORE, "time", System.currentTimeMillis())
//            startActivity(Intent(this, FirstLaunchActivity::class.java))
//            finish()
//        }
        //////////////////////////// TODO{отделить}
        val checkTimeScore =
            newPrefs.getLong(PreferencesUtils.PREF_SCORE, "time", System.currentTimeMillis())
        val multiplierScore =
            mutableStateOf(newPrefs.getInt(PreferencesUtils.PREF_SCORE, "multiplierScore", 1))
        val score = mutableStateOf(newPrefs.getInt(PreferencesUtils.PREF_SCORE, "score", 1))
        if (checkTimeScore < System.currentTimeMillis()) {
            if (checkTimeScore + 86400000 < System.currentTimeMillis()) {
                if (checkTimeScore + 172800000 > System.currentTimeMillis()) {
                    multiplierScore.value += 1
                    newPrefs.putInt(
                        PreferencesUtils.PREF_SCORE,
                        "multiplierScore",
                        multiplierScore.value
                    )
                }
            }
        } else {
            multiplierScore.value = 1
            newPrefs.putInt(PreferencesUtils.PREF_SCORE, "multiplierScore", multiplierScore.value)
        }
        /////////////////////
//        val locale = Locale("en")
//        Locale.setDefault(locale)
//        val config = this.resources.configuration
//        config.setLocale(locale)
//        this.createConfigurationContext(config)
//        @Suppress("DEPRECATION")
//        this.resources.updateConfiguration(config, this.resources.displayMetrics)

//        badgeCount.value = dayReminderMap.size
//        if (badgeCount.value > 100) {
//            badgeCount.value = 99
//        }
        lifecycleScope.launch {
            dogsInfoViewModel.dogProfiles.collect { dogs ->
                if (dogs.isNotEmpty()) {
                    dogsProfileArray.clear()
                    dogsProfileArray.addAll(dogs)
                    currentTimeInMinutes = dogsProfileArray[0].currentTimeWalk
                    dogsProfileArray.forEach {
                        val sdf = SimpleDateFormat("dd.MM.yyyy")
                        val dateLast = Date(it.lastWalk.time) //23.07
                        val dateNow = Date(Calendar.getInstance().timeInMillis) //26.07
                        //Убрал проверку на isDateChecked
                        if ((sdf.parse(sdf.format(dateLast)) < sdf.parse(sdf.format(dateNow)))) {
                            dogsInfoViewModel.updateDogsDate(
                                it.id,
                                Date(Calendar.getInstance().timeInMillis)
                            )
                            dogsInfoViewModel.updateDogsTime(it.id, it.walkingTimeConst)
                            currentTimeInMinutes = it.walkingTimeConst
                            Log.d("check", "Меньше")
                        } else {
                            Log.d("check", "Больше")
                        }
                    }
                }
            }
        }
        //TODO{СДЕЛАТЬ ОБНОВЛЕНИЕ STATS ПОСЛЕ ОКОНЧАНИЯ ТАЙМЕРА}
        lifecycleScope.launch {
            myPetPaddockT.collect {
                score.value = newPrefs.getInt(PreferencesUtils.PREF_SCORE, "score", 1)
                try {
                    if (dogsProfileArray.isNotEmpty()) {
                        dogsInfoViewModel.updateDogsTime(
                            dogsProfileArray[currentDogId.value].id,
                            currentTimeInMinutes
                        )
                    }
                } catch (e: Exception) {
                    Log.e("Exception", e.toString())
                }
            }
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val tabs = remember { BottomTabs.values() }
            val navController = rememberNavController()
            ApplicationTheme(darkMode.isNightMode.value) {
                Scaffold(
                    bottomBar = { BottomNav(navController, tabs) },
                    scaffoldState = scaffoldState
                ) {
                    if (badgeCount.value > 0 && !isBadgeShown.value) {
                        isBadgeShown.value = true
                        LaunchedEffect(scope) {
                            scaffoldState.snackbarHostState.showSnackbar("У тебя ${badgeCount.value} истекающих напоминания")
                        }
                    }
                    AppNavGraph(navController = navController)
                }

            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MenuScreen(navController: NavController) {
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(onClick = {
                startActivity(
                    Intent(
                        this@MainActivity,
                        DocumentActivity::class.java
                    )
                )
            }, shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.menu_menu_button_document_analyzes_text), //Документы/Анализы
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
            Card(onClick = {
                navController.navigate(NavGraphRoutes.vaccination.route)
            }, shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.menu_menu_button_vaccine_text), //Vaccination
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
//                        Card(onClick = {  }, enabled = false) {
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .background(mainSecondColor)
//                                    .padding(
//                                        start = 20.dp,
//                                        end = 20.dp,
//                                        top = 10.dp,
//                                        bottom = 10.dp
//                                    ),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Row(
//                                    Modifier.padding(top = 10.dp, bottom = 10.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Column(horizontalAlignment = Alignment.Start) {
//                                        Text(text = "Настройки", fontSize = 18.sp)
//                                    }
//                                }
//                            }
//                        }

//                        Card(onClick = { startActivity(Intent(this@MainActivity, RoadMapActivity::class.java)) }) {
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .background(mainSecondColor)
//                                    .padding(
//                                        start = 20.dp,
//                                        end = 20.dp,
//                                        top = 10.dp,
//                                        bottom = 10.dp
//                                    ),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Row(
//                                    Modifier.padding(top = 10.dp, bottom = 10.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Column(horizontalAlignment = Alignment.Start) {
//                                        Text(text = "Дорожная карта", fontSize = 18.sp)
//                                    }
//                                }
//                            }
//                        }
            Card(onClick = {
                navController.navigate(NavGraphRoutes.aboutAuthor.route)
            }, shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.menu_menu_button_about_author), //about author
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
            Card(onClick = {
                navController.navigate(NavGraphRoutes.dogNutrition.route)
            }, shape = RoundedCornerShape(0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.menu_menu_button_barf_nutrition), //barf nutrition
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = resources.getString(R.string.dark_mode_text),
                    fontSize = 18.sp,
                    color = mainTextColor
                )//Dark mode
                Switch(
                    checked = darkMode.isNightMode.value, onCheckedChange = {
                        darkMode.setDarkMode(it)
                        newPrefs.putBoolean(
                            PreferencesUtils.PREF_NIGHT_MODE,
                            "isNightModeOn",
                            it
                        ); CheckDarkMode.isDarkMode(it)
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = switcherColor)
                )
            }
//                            Card(onClick = {
//                            }, shape = RoundedCornerShape(0.dp)) {
//                                Row(
//                                    Modifier
//                                        .fillMaxWidth()
//                                        .background(mainSecondColor)
//                                        .padding(
//                                            start = 20.dp,
//                                            end = 20.dp,
//                                            top = 10.dp,
//                                            bottom = 10.dp
//                                        ),
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Row(
//                                        Modifier.padding(top = 10.dp, bottom = 10.dp).fillMaxWidth(),
//                                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                            Text(
//                                                text = resources.getString(R.string.coins_count_text), //coins count
//                                                fontSize = 18.sp,
//                                                color = mainTextColor
//                                            )
//                                            Text(
//                                                text = (score.value.toString()+"x"+multiplierScore.value.toString()),
//                                                fontSize = 18.sp,
//                                                color = mainTextColor
//                                            )
//
//                                    }
//                                }
//                            }
        }
    }


    @Composable
    fun DogsKnowledesScreen(
        navController: NavController,
        viewModel: DogsEncyclopediaViewModel = hiltViewModel()
    ) {
        val dogsList = viewModel.groups.collectAsState(initial = dogsEncyclopedia)
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .padding(bottom = 50.dp)
        ) {
            titleText(resources.getString(R.string.encyclopedia_menu_title)) //Энциклопедия
            listDogs(navController, dogsList.value)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun KnowledgesScreen(navController: NavController) {
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {
            titleText(resources.getString(R.string.useful_menu_title_text)) //полезные статьи
            Card(
                onClick = {
                    val intent = Intent(
                        this@MainActivity,
                        ArticleChoiceActivity::class.java
                    )
                    intent.putExtra("Article", "feeding");startActivity(intent)
                },
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                backgroundColor = mainSecondColor
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.useful_menu_feeding_text), //Кормление
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
            Card(
                onClick = {
                    val intent = Intent(
                        this@MainActivity,
                        ArticleChoiceActivity::class.java
                    )
                    intent.putExtra("Article", "activity");startActivity(intent)
                },
                modifier = Modifier.padding(
                    top = 10.dp,
                    start = 15.dp,
                    end = 15.dp
                ), backgroundColor = mainSecondColor
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = resources.getString(R.string.useful_menu_activity_text), //Активность
                                fontSize = 18.sp,
                                color = mainTextColor
                            )

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HomeScreen(navController: NavController/*, viewModel: DogsInfoViewModel = hiltViewModel()*/) {
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .verticalScroll(rememberScrollState(), isScreenHorizontal(this))
                .padding(start = 0.dp, end = 0.dp)
        ) {
            titleText(resources.getString(R.string.main_menu_title_text))
            if (dogsProfileArray.isNotEmpty()) {
                Dashboard(dogsProfileArray)
            }
            if (dogsProfileArray.isNotEmpty()) {
                Stats()
            }
            Column(Modifier.padding(start = 15.dp, end = 15.dp, top = 20.dp)) {
                OutlinedButton(
                    onClick = {
//                        TODO{поменять на интент, ибо не сделал WalkScreen}
//                          navController.navigate(NavGraphRoutes.walkLaunch.route)
                        currentTimeInMinutes =
                            dogsProfileArray[currentDogId.value].currentTimeWalk
                        val intent = Intent(
                            this@MainActivity,
                            WalkLaunchActivity::class.java
                        )
                        intent.putExtra("id", currentDogId.value)
                        startActivity(
                            intent
                        )
                    },
                    Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                        .height(70.dp)
                        .fillMaxWidth(), shape = RoundedCornerShape(35),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = homeButtonColor,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        resources.getString(R.string.main_menu_button_walk_text),
                        color = mainTextColor
                    ) //Прогулка
                }
//                            OutlinedButton(
//                                onClick = {
//                                }, modifier = Modifier
//                                    .padding(
//                                        start = 20.dp, top = 20.dp,
//                                        end = 20.dp
//                                    )
//                                    .height(70.dp)
//                                    .fillMaxWidth()
//                                ,shape = RoundedCornerShape(35), enabled = false,
//                                colors = ButtonDefaults.buttonColors(
//                                    backgroundColor = homeButtonColor,
//                                    contentColor = Color.Black
//                                ),
//                            ) {
//                                Text("Достижения")
//                            }
            }
        }
    }


    @Composable
    fun Stats() {
//        dogsProfileArray[currentDogId.value]
        val res = remember {
            mutableStateOf(/*(timer[currentDogId.value].currentTimeWalk / timer[currentDogId.value].walkingTimeConst).toFloat()*/
                0F
            )
        }  //0.836
        val resReverse = remember { mutableStateOf(/*1F - res.value*/0F) }  // 0,164

        LaunchedEffect(rememberCoroutineScope()) {
            currentDogId.collect {
                res.value =
                    (dogsProfileArray[currentDogId.value].currentTimeWalk.toFloat() / dogsProfileArray[currentDogId.value].walkingTimeConst.toFloat())
                resReverse.value = (1F - res.value)
            }
        }
//        if (dogsProfileArray.isNotEmpty()) {
//            LaunchedEffect(rememberCoroutineScope()) {lifecycleScope.launch{
//                res.value = (dogsProfileArray[currentDogId.value].currentTimeWalk.toFloat() / (dogsProfileArray[currentDogId.value].walkingTimeConst.toFloat()))
//                resReverse.value = 1F - res.value
//            }}
//        }

        val availableProgressColor = when {
            res.value < 0.35F -> Color(0xFFFB3640) //green
            res.value < 0.75F -> Color(0xFFffca3a) //yellow
            else -> Color(0xFF8ac926) //red
        }

        val planProgressColor = when {
            resReverse.value < 0.35F -> Color(0xFFFB3640)//green
            resReverse.value < 0.75F -> Color(0xFFffca3a)//yellow
            else -> Color(0xFF8ac926)//red
        }

        LaunchedEffect(rememberCoroutineScope()) {
            lifecycleScope.launch {
                if ((resReverse.value * 100).toInt() + (res.value * 100).toInt() < 100) {
                    if ((resReverse.value * 100).toInt() != 0 && (resReverse.value * 100).toInt() != 100) {
                        resReverse.value += 0.01F
                    }
                }
            }
        }

        Text(
            text = resources.getString(R.string.main_menu_stats_text), //Статистика
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            color = mainTextColor
        )
        Card(
            shape = RoundedCornerShape(10.dp), backgroundColor = mainSecondColor,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(
                        text = resources.getString(R.string.main_menu_stats_today_plan_text), //Сегодняшний план
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = mainTextColor
                    )
                    Text(
                        text = "${(resReverse.value * 100).toInt()}% " + resources.getString(R.string.main_menu_stats_today_plan_done_text), //выполнено
                        color = Color.Gray
                    )
                }
                CircularProgressIndicator(
                    progress = resReverse.value,
                    color = planProgressColor,
                    strokeWidth = 5.dp
                )
            }
        }
        Card(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp),
            shape = RoundedCornerShape(10.dp), backgroundColor = mainSecondColor
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(
                        text = resources.getString(R.string.main_menu_stats_energy_enough), //Энергии доступно
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = mainTextColor
                    )
//                    Text(text = res.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${(res.value * 100).toInt()}% " + resources.getString(R.string.main_menu_stats_energy_enough_remained),
                        color = Color.Gray
                    ) //энергии
                }
                CircularProgressIndicator(
                    progress = res.value,
                    color = availableProgressColor,
                    strokeWidth = 5.dp
                )
            }
        }
    }

    @Composable
    fun AppNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
        NavHost(navController = navController, startDestination = NavGraphRoutes.home.route) {
            composable(NavGraphRoutes.home.route) {
                HomeScreen(navController = navController)
            }
            composable(NavGraphRoutes.knowledges.route) {
                KnowledgesScreen(navController = navController)
            }
            composable(NavGraphRoutes.dogCards.route) {
//              val viewModel: DogsBreedEncyclopediaViewModel = hiltViewModel()
                DogsKnowledesScreen(navController = navController/*, viewModel*/)
            }
            composable(NavGraphRoutes.detailedDogs.route + "/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType;defaultValue = 0;nullable = false
                })
            ) {
//              val viewModel: DogsBreedEncyclopediaViewModel = hiltViewModel()
                DetailedDogScreen(
                    navController = navController,
                    it.arguments!!.getInt("id")/*, viewModel*/
                )
            }
            composable(NavGraphRoutes.health.route) {
                ReminderScreen(navController = navController)
            }
            composable(NavGraphRoutes.menu.route) {
                MenuScreen(navController = navController)
            }
            composable(NavGraphRoutes.addReminder.route) {
//              val reminderViewModel = hiltViewModel<ReminderViewModel>()
                AddReminderScreen(navController = navController /*reminderViewModel*/)
            }
            composable(NavGraphRoutes.vaccination.route) {
//              val viewModel: VaccinationViewModel = hiltViewModel()
                VaccinationScreen(navController = navController/*, viewModel = viewModel*/)
            }
            composable(NavGraphRoutes.addVaccination.route) {
//              val viewModel: VaccinationViewModel = hiltViewModel()
                AddVaccination(navController = navController/*,viewModel = viewModel*/)
            }
//          composable(NavGraphRoutes.walkLaunch.route) {
//              WalkLaunchScreen(navController, timer)
//          }
            composable(NavGraphRoutes.aboutAuthor.route) {
                AboutAuthorScreen(navController = navController)
            }
            composable(NavGraphRoutes.dogNutrition.route) {
                DogNutritionScreen(navController = navController)
            }
        }
    }

    @Composable
    fun BottomNav(navController: NavController, tabs: Array<BottomTabs>) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: BottomTabs.HOME.route

        val routes = remember { BottomTabs.values().map { it.route } }
        if (currentRoute in routes) {
            BottomNavigation(backgroundColor = mainBackgroundColor) {
                tabs.forEach { tab ->
                    BottomNavigationItem(icon = {
                        Icon(
                            painterResource(id = tab.icon),
                            modifier = Modifier.size(25.dp),
                            contentDescription = "",
                            tint = mainTextColor
                        )
                    },
                        selected = currentRoute == tab.route, onClick = {
                            if (tab.route != currentRoute) {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }, modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
//        BottomNavigation(backgroundColor = mainBackgroundColor) {
//            val home = R.drawable.home_48px
//            val knowledges = R.drawable.school_48px
//            val dogCards = R.drawable.menu_book_48px
//            val health = R.drawable.health_and_safety_48px
//            val menu = Icons.Outlined.Menu
//            BottomNavigationItem(
//                icon = {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(home),
//                        "",
//                        modifier = Modifier.size(25.dp),
//                        tint = mainTextColor
//                    )
//                },
////            label = { Text(text = "", fontSize = 9.sp) }, //Питомец
//                selected = (selectedIndex.value == 0),
//                onClick = {
//                    selectedIndex.value = 0
//                })
//            BottomNavigationItem(icon = {
//                Icon(
//                    imageVector = ImageVector.vectorResource(knowledges),
//                    "",
//                    modifier = Modifier.size(25.dp), tint = mainTextColor
//                )
//            },
////            label = { Text(text = "", fontSize = 9.sp) },//Знания
//                selected = (selectedIndex.value == 1),
//                onClick = {
//                    selectedIndex.value = 1
//                })
//            BottomNavigationItem(icon = {
//                Icon(
//                    imageVector = ImageVector.vectorResource(dogCards),
//                    "",
//                    modifier = Modifier.size(25.dp),
//                    tint = mainTextColor
//                )
//            },
////            label = { Text(text = "", fontSize = 9.sp) },//Томик
//                selected = (selectedIndex.value == 2),
//                onClick = {
//                    selectedIndex.value = 2
//                })
//            BottomNavigationItem(icon = {
//                if (badgeCount.value > 0) {
//                    BadgedBox(badge = { Text(text = badgeCount.value.toString(), modifier = Modifier
//                        .background(Color.Red, shape = RoundedCornerShape(10.dp))
//                        .padding(
//                            when {
//                                badgeCount.value < 10 -> 7.dp
//                                badgeCount.value < 100 -> 5.dp
//                                else -> 5.dp
//                            }, 0.dp, when {
//                                badgeCount.value < 10 -> 7.dp
//                                badgeCount.value < 100 -> 5.dp
//                                else -> 5.dp
//                            }, 0.dp
//                        ), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold
//                    )
//                    }) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(health),
//                            "",
//                            modifier = Modifier.size(25.dp),
//                            tint = mainTextColor
//                        )
//                    }
//                } else {
//                    Icon(
//                        imageVector = ImageVector.vectorResource(health),
//                        "",
//                        modifier = Modifier.size(25.dp),
//                        tint = mainTextColor
//                    )
//                }
//            },
////            label = { Text(text = "", fontSize = 9.sp) }, //Здоровье
//                selected = (selectedIndex.value == 3),
//                onClick = {
//                    selectedIndex.value = 3
//                })
//            BottomNavigationItem(icon = {
//                Icon(imageVector = menu, "", modifier = Modifier.size(25.dp), tint = mainTextColor)
//            },
////                label = { Text(text = "", fontSize = 9.sp) }, //Меню
//                selected = (selectedIndex.value == 4),
//                onClick = {
//                    selectedIndex.value = 4
//                })
//        }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListRowDogs(model: DogsBreedEncyclopediaEntity, navController: NavController) {
        //r?
        Card(
            onClick = {
                navController.currentBackStackEntry?.arguments?.putInt("id", model.id)
                navController.navigate(NavGraphRoutes.detailedDogs.route + "/${model.id}")
//                val intent = Intent(this@MainActivity, DetailedDogActivity::class.java)
//                intent.putExtra("id", model.id)
//                startActivity(intent)
            },
            modifier = Modifier.padding(top = 10.dp),
            shape = RoundedCornerShape(0.dp)
        )
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (model.imageFile.isNotBlank()) {
                        Image(
                            painterResource(
                                resources.getIdentifier(model.imageFile, "drawable", packageName)
                            ), contentDescription = "", contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(75.dp)
                                .clip(RoundedCornerShape(100))
                        )
                    }
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = model.breedName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = mainTextColor
                        )
                        Text(
                            text = model.origin,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis, color = mainTextColor
                        )
                        Text(
                            text = model.lifeSpan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis, color = mainTextColor
                        )
                    }
                }
                Column(
                    Modifier.padding(end = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = model.litterSize, color = mainTextColor)
                }
            }
        }
    }

    @Composable
    fun listDogs(navController: NavController, dogList: List<DogsBreedEncyclopediaEntity>) {
        LazyColumn {
            items(dogList) { item ->
                ListRowDogs(item, navController)
            }
        }
    }

    @Composable
    fun titleText(title: String) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Box(Modifier.weight(0.5f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                    color = mainTextColor
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    @Composable
    fun Dashboard(list: MutableList<DogsInfoEntity>) {
        val ctx = LocalContext.current
        val pagerState = rememberPagerState()
        Text(
            text = resources.getString(R.string.main_menu_profile_text), //Профиль
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            color = mainTextColor
        )
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = homeButtonColor,
//            contentColor = Color.Black
//        )
        HorizontalPager(count = list.size, state = pagerState) {
            currentDogId.value = pagerState.currentPage
            currentTimeById = dogsProfileArray[pagerState.currentPage].currentTimeWalk.toFloat()
            currentTimeConstById =
                dogsProfileArray[pagerState.currentPage].walkingTimeConst.toFloat()
            Card(
                onClick = {
                    val intent = Intent(ctx, NewPetActivity::class.java)
                    intent.putExtra("id", currentDogId.value)
                    startActivity(intent)
                },
                elevation = 15.dp,
                shape = RoundedCornerShape(15),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                backgroundColor = mainSecondColor
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
//                        .background(mainSecondColor)
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (list[it].image.isNotEmpty()) {
                            GlideImage(
                                imageModel = Uri.parse(list[it].image), modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(75.dp)
                                    .clip(RoundedCornerShape(100)), contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painterResource(R.drawable.avatar_paw),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(75.dp)
                                    .clip(RoundedCornerShape(100))
                            )
                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = list[it].breedName, color = mainTextColor)
                            Text(
                                text = list[it].name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = mainTextColor
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "", tint = mainTextColor)
//                            when {
//                                list[it].currentTimeWalk == list[it].walkingTimeConst -> Text((list[it].walkingTimeConst.toLong()/60000).toString(), color = Color.Blue)
//                                list[it].currentTimeWalk < list[it].walkingTimeConst -> Text((list[it].currentTimeWalk.toLong()/60000).toString(), color = Color.Blue)
//                            }
                    }
                }
            }
        }
        if (list.size >= 2) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalPagerIndicator(pagerState = pagerState, activeColor = mainTextColor)
            }
        }
    }

    private fun isScreenHorizontal(ctx: Context): Boolean {
        return ctx.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}


