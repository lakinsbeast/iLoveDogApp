package com.sagirov.ilovedog.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.MutableLiveData
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
import com.sagirov.ilovedog.*
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoViewModel
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.Screens.DetailedDog.DetailedDogScreen
import com.sagirov.ilovedog.Screens.Vaccinations.AddVaccination
import com.sagirov.ilovedog.Screens.Vaccinations.VaccinationScreen
import com.sagirov.ilovedog.Utils.CheckDarkModeManager
import com.sagirov.ilovedog.Utils.PreferencesUtils
import com.sagirov.ilovedog.ViewModels.*
import com.sagirov.ilovedog.analytics.google.GMS
import com.sagirov.ilovedog.analytics.huawei.HMS
import com.sagirov.ilovedog.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var HMSUtils: HMS
    @Inject
    lateinit var GMSUtils: GMS

    @Inject
    lateinit var newPrefs: PreferencesUtils

    @Inject
    lateinit var darkMode: CheckDarkModeManager

    companion object {
        var myPetPaddockT = MutableLiveData(false)
    }

    private var badgeCount = mutableStateOf(0)
    private var isBadgeShown = mutableStateOf(false)

    private var dateForVisitToVet = mutableMapOf<Long, String>()
    private var pastReminderMap = mutableStateMapOf<Long, String>()
    private var pastReminderIds = mutableListOf<Int>()
    private var weeklyReminderMap = mutableStateMapOf<Long, String>()
    private var weeklyReminderIds = mutableListOf<Int>()
    private var otherReminderMapp = mutableStateMapOf<Long, String>()
    private var otherReminderIds = mutableListOf<Int>()
    private var dayReminderMap = mutableStateMapOf<Long, String>()
    private var dayReminderIds = mutableListOf<Int>()
    private var repeatReminderMap = mutableStateMapOf<Long, String>()
    private var repeatReminderIds = mutableListOf<Int>()
    private val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()
    private val dogsProfileArray = mutableStateListOf<DogsInfoEntity>()
    private val currentDogId = mutableStateOf(0)

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels()
//    private val dogsBreedViewModel: DogsBreedEncyclopediaViewModel by viewModels()
//    private val remindersViewModel: ReminderViewModel by viewModels()
//    private val vaccineViewModel: VaccinationViewModel by viewModels()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HMSUtils = HMS()
        GMSUtils = GMS()
        if (HMSUtils.checkHuaweiApi(this)) {
            HMSUtils.enableHuaweiAnalytics(this)
        }
        if (GMSUtils.checkGoogleApi(this)) {
            GMSUtils.enableGoogleAnalytics(this)
        }
        newPrefs = PreferencesUtils(this)
        darkMode = CheckDarkModeManager(this)
        val checkTimeScore = newPrefs.getLong(PreferencesUtils.PREF_SCORE, "time", System.currentTimeMillis())
        val multiplierScore = mutableStateOf(newPrefs.getInt(PreferencesUtils.PREF_SCORE, "multiplierScore", 1))
        val score = mutableStateOf(newPrefs.getInt(PreferencesUtils.PREF_SCORE, "score", 1))
        if (checkTimeScore < System.currentTimeMillis()) {
            if (checkTimeScore + 86400000 < System.currentTimeMillis()) {
                if (checkTimeScore + 172800000 > System.currentTimeMillis()) {
                    multiplierScore.value += 1
                    newPrefs.putInt(PreferencesUtils.PREF_SCORE, "multiplierScore", multiplierScore.value)
                }
            }
        } else {
            multiplierScore.value = 1
            newPrefs.putInt(PreferencesUtils.PREF_SCORE, "multiplierScore", multiplierScore.value)
        }

//        val locale = Locale("en")
//        Locale.setDefault(locale)
//        val config = this.resources.configuration
//        config.setLocale(locale)
//        this.createConfigurationContext(config)
//        @Suppress("DEPRECATION")
//        this.resources.updateConfiguration(config, this.resources.displayMetrics)

        darkMode.checkDarkMode()

        val frst_lnch = newPrefs.getBoolean(PreferencesUtils.PREF_NAME, "firstOpen", true)
        if (frst_lnch) {
            newPrefs.putLong(PreferencesUtils.PREF_SCORE, "time", System.currentTimeMillis())
            startActivity(Intent(this, FirstLaunchActivity::class.java))
            finish()
        }

//        remindersViewModel.getAllReminders.observe(this) { list ->
//            dateForVisitToVet.clear()
//            list.forEach { listfeach ->
//                listfeach.reminder.forEach {
////                    dateForVisitToVet[it.key.toLong()] = it.value
//                    try {
//                        when {
//                            it.value.contains(".Повтор%#%:#%:") -> {repeatReminderMap[it.key.toLong()] =
//                                it.value.substringBefore(".Повтор"); repeatReminderIds.add(listfeach.id)}
//                            it.key.toLong() < System.currentTimeMillis() ->{pastReminderMap[it.key.toLong()] = it.value;
//                                pastReminderIds.add(listfeach.id)}
//                            System.currentTimeMillis() + 86400000 > it.key.toLong() -> {
//                                dayReminderMap[it.key.toLong()] =
//                                    it.value; dayReminderIds.add(listfeach.id)
//                            }
//                            System.currentTimeMillis() + 604800000 > it.key.toLong() -> {
//                                weeklyReminderMap[it.key.toLong()] =
//                                    it.value; weeklyReminderIds.add(listfeach.id)
//                            }
//                            else -> {
//                                otherReminderMapp[it.key.toLong()] = it.value;otherReminderIds.add(listfeach.id)
//                            }
//                        }
//                    } catch (e: Exception) {
//                        Log.d("excp", e.toString())
//                    }
//                }
//            }
//            dateForVisitToVet.forEach {
//                Log.d("LiveData", it.key.toString())
//                Log.d("LiveData", it.value.toString())
//            }
//        }
        badgeCount.value = dayReminderMap.size
        if (badgeCount.value > 100) {
            badgeCount.value = 99
        }



        dogsInfoViewModel.getAllDogsProfiles.observe(this) { dogs ->
            dogsProfileArray.clear()
            dogsProfileArray.addAll(dogs)
            currentTimeInMinutes = dogsProfileArray[0].currentTimeWalk
            dogsProfileArray.forEach {
                Log.d("profiles", it.toString())
                val sdf = SimpleDateFormat("dd.MM.yyyy")
                val dateLast = Date(it.lastWalk.time) //23.07
                val dateNow = Date(Calendar.getInstance().timeInMillis) //26.07
                //Убрал проверку на isDateChecked
                if ((sdf.parse(sdf.format(dateLast)) < sdf.parse(sdf.format(dateNow))) /*&& !isDateChecked*/) {
                    dogsInfoViewModel.updateDogsDate(it.id, Date(Calendar.getInstance().timeInMillis))
                    dogsInfoViewModel.updateDogsTime(it.id, it.walkingTimeConst)
                    currentTimeInMinutes = it.walkingTimeConst
                    Log.d("check", "Меньше")
//                    isDateChecked = true
                } else {
                    Log.d("check", "Больше")
//                    isDateChecked = true
                }
            }
        }
//        dogsBreedViewModel.allDogs.observe(this) { list ->
//            list.forEach {
//                dogsEncyclopedia.add(
//                    DogsBreedEncyclopediaEntity(
//                        it.id,
//                        it.breedName,
//                        it.origin,
//                        it.breedGroup,
//                        it.lifeSpan,
//                        it.type,
//                        it.temperament,
//                        it.male_height,
//                        it.female_height,
//                        it.male_weight,
//                        it.female_weight,
//                        it.colors,
//                        it.litterSize,
//                        it.description,
//                        it.imageFile
//                    )
//                )
//            }
//        }
        myPetPaddockT.observe(this) {
            score.value = newPrefs.getInt(PreferencesUtils.PREF_SCORE, "score", 1)
            try {
                if (dogsProfileArray.isNotEmpty()) {
//                    dogsInfoViewModel.updateDogsTime(dogsProfileArray[currentDogId.value].id, currentTimeInMinutes)
                }
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
            }
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val tabs = remember { BottomTabs.values()}
            val navController = rememberNavController()
            ApplicationTheme(darkMode.isNightMode.value) {
                Scaffold(bottomBar = { BottomNav(navController, tabs) }, scaffoldState = scaffoldState) {
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
                       navController.navigate("vaccination")
//                startActivity(
//                    Intent(
//                        this@MainActivity,
//                        VaccinationActivity::class.java
//                    )
//                )
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
                startActivity(
                    Intent(
                        this@MainActivity,
                        AboutAuthorPageActivity::class.java
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
                                text = resources.getString(R.string.menu_menu_button_about_author), //about author
                                fontSize = 18.sp,
                                color = mainTextColor
                            )
                        }
                    }
                }
            }
            Card(onClick = {
                startActivity(
                    Intent(
                        this@MainActivity,
                        DogNutritionActivity::class.java
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
                    checked = /*isNightMode.value*/ darkMode.isNightMode.value, onCheckedChange = {
                        /*isNightMode.value = it*/ darkMode.setDarkMode(it);
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
    fun ReminderScreen(navController: NavController, viewModel: ReminderViewModel = hiltViewModel()) {
            viewModel.getAllReminders.observe(this) { list ->
                dateForVisitToVet.clear()
                repeatReminderMap.clear()
                pastReminderMap.clear()
                dayReminderMap.clear()
                weeklyReminderMap.clear()
                otherReminderMapp.clear()
                list.forEach { listfeach ->
                    listfeach.reminder.forEach {
    //                    dateForVisitToVet[it.key.toLong()] = it.value
                        try {
                            when {
                                it.value.contains(".Повтор%#%:#%:") -> {repeatReminderMap[it.key.toLong()] =
                                    it.value.substringBefore(".Повтор"); repeatReminderIds.add(listfeach.id)}
                                it.key.toLong() < System.currentTimeMillis() ->{pastReminderMap[it.key.toLong()] = it.value;
                                    pastReminderIds.add(listfeach.id)}
                                System.currentTimeMillis() + 86400000 > it.key.toLong() -> {
                                    dayReminderMap[it.key.toLong()] =
                                        it.value; dayReminderIds.add(listfeach.id)
                                }
                                System.currentTimeMillis() + 604800000 > it.key.toLong() -> {
                                    weeklyReminderMap[it.key.toLong()] =
                                        it.value; weeklyReminderIds.add(listfeach.id)
                                }
                                else -> {
                                    otherReminderMapp[it.key.toLong()] = it.value;otherReminderIds.add(listfeach.id)
                                }
                            }
                        } catch (e: Exception) {
                            Log.d("excp", e.toString())
                        }
                    }
                }
        }
//        val getArrayFromJson = newPrefs.getString(PREF_NAME_DATES, "dateForVisitToVet", "")
//        if (getArrayFromJson != "") {
//            dateForVisitToVet =
//                (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
//            val it = dateForVisitToVet.iterator()
//            while (it.hasNext()) {
//                val item = it.next()
//                when {
//                    item.value.contains(".Повтор%#%:#%:") -> repeatReminderMap[item.key] =
//                        item.value.substringBefore(".Повтор")
//                    item.key < System.currentTimeMillis() -> pastReminderMap[item.key] = item.value
//                    System.currentTimeMillis() + 86400000 > item.key -> dayReminderMap[item.key] =
//                        item.value
//                    System.currentTimeMillis() + 604800000 > item.key -> weeklyReminderMap[item.key] =
//                        item.value
//                    else -> otherReminderMapp[item.key] = item.value
//                }
//            }
//        }
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = {
                    navController.navigate(NavGraphRoutes.addReminder.route)
//                    startActivity(
//                        Intent(
//                            this@MainActivity,
//                            ReminderActivity::class.java
//                        )
//                    )
                },
                Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    )
                    .height(70.dp)
                    .fillMaxWidth()
                    .border(
                        width = 0.dp, color = Color.Black,
                        shape = RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = mainSecondColor,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    resources.getString(R.string.reminder_menu_button_text),
                    color = mainTextColor
                ) //Добавить напоминание
            }
            /*
            * Я поменял внутри циклов items с item.value На item.key
            * */
            if (repeatReminderMap.isNotEmpty()) {
                Text(
                    resources.getString(R.string.reminder_menu_reminder_repeat),
                    color = mainTextColor
                ) //Повторяющиеся напоминания
                RepeatReminderColumn(repeatReminderIds,data = repeatReminderMap, viewModel)
            }
            if (dayReminderMap.isNotEmpty()) {
                Text(
                    resources.getString(R.string.reminder_menu_reminder_day),
                    color = mainTextColor
                ) //Дневные напоминания
                DayReminderColumn(dayReminderIds,data = dayReminderMap, viewModel)
            }
            if (weeklyReminderMap.isNotEmpty()) {
                Text(
                    resources.getString(R.string.reminder_menu_reminder_week),
                    color = mainTextColor
                ) //Недельные напоминания
                WeeklyReminderColumn(weeklyReminderIds,data = weeklyReminderMap, viewModel)
            }
            if (otherReminderMapp.isNotEmpty()) {
                Text(
                    resources.getString(R.string.reminder_menu_reminder_other),
                    color = mainTextColor
                ) //Остальные напоминания
                OtherReminderLazyColumn(otherReminderIds, data = otherReminderMapp, viewModel)
            }
            if (pastReminderMap.isNotEmpty()) {
                Text(
                    resources.getString(R.string.reminder_menu_reminder_past),
                    color = mainTextColor
                ) //Прошедшие напоминания
                PastReminderColumn(pastReminderIds, data = pastReminderMap, viewModel)
            }

        }
    }

    @Composable
    fun DogsKnowledesScreen(navController: NavController, viewModel: DogsBreedEncyclopediaViewModel = hiltViewModel()) {
        viewModel.allDogs.observe(this) { list ->
            Log.d("dogsEncyclo", "сработал лайвдата")
            dogsEncyclopedia.clear()
            list.forEach {
                dogsEncyclopedia.add(
                    DogsBreedEncyclopediaEntity(
                        it.id,
                        it.breedName,
                        it.origin,
                        it.breedGroup,
                        it.lifeSpan,
                        it.type,
                        it.temperament,
                        it.male_height,
                        it.female_height,
                        it.male_weight,
                        it.female_weight,
                        it.colors,
                        it.litterSize,
                        it.description,
                        it.imageFile
                    )
                )
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
                .padding(bottom = 50.dp)
        ) {
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(start = 15.dp, end = 15.dp)) {
//                            Box(Modifier.weight(0.5f)) {
//                                Text(
//                                    text = "Энциклопедия",
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 36.sp
//                                )
//                            }
//                        }
            titleText(resources.getString(R.string.encyclopedia_menu_title)) //Энциклопедия
            listDogs(navController)
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
//                            .padding(start = 15.dp, end = 15.dp)
        ) {
//                        Row(Modifier.fillMaxWidth()) {
//                            Box(Modifier.weight(0.5f)) {
//                                Text(
//                                    text = "Полезные статьи",
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 36.sp
//                                )
//                            }
//                        }
            titleText(resources.getString(R.string.useful_menu_title_text)) //полезные статьи

            Card(
                onClick = {
                    val intent = Intent(
                        this@MainActivity,
                        ArticleChoiceActivity::class.java
                    );
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
                    );
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
    fun HomeScreen(navController: NavController/*, viewModel: DogsInfoViewModel = hiltViewModel()*/){
//        viewModel.getAllDogsProfiles.observe(this) { dogs ->
//            dogsProfileArray.clear()
//            currentTimeInMinutes = 0
//            dogsProfileArray.addAll(dogs)
//            currentTimeInMinutes = dogsProfileArray[0].currentTimeWalk
//            dogsProfileArray.forEach {
//                Log.d("profiles", it.toString())
//                val sdf = SimpleDateFormat("dd.MM.yyyy")
//                val dateLast = Date(it.lastWalk.time) //23.07
//                val dateNow = Date(Calendar.getInstance().timeInMillis) //26.07
//                //Убрал проверку на isDateChecked
//                if ((sdf.parse(sdf.format(dateLast)) < sdf.parse(sdf.format(dateNow))) /*&& !isDateChecked*/) {
//                    viewModel.updateDogsDate(it.id, Date(Calendar.getInstance().timeInMillis))
//                    viewModel.updateDogsTime(it.id, it.walkingTimeConst)
//                    currentTimeInMinutes = it.walkingTimeConst
//                    Log.d("check", "Меньше")
////                    isDateChecked = true
//                } else {
//                    Log.d("check", "Больше")
////                    isDateChecked = true
//                }
//            }
//        }
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
                Stats(dogsProfileArray)
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun OtherReminderLazyColumn(id: List<Int>,data: Map<Long, String>, remindersViewModel: ReminderViewModel = hiltViewModel()) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        dateForVisitToVet.remove(it.key)
                        otherReminderMapp.remove(it.key)
                        remindersViewModel.deleteReminder(mapOf(it.key.toString() to it.value.toString()))
                    }
                    alertDelete.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }
                    Card(
                        onClick = {
                            val alert = android.app.AlertDialog.Builder(this@MainActivity)
//                        alert.setTitle(it.value)
                            alert.setMessage(it.value)
                            alert.setCancelable(true)
                            alert.setPositiveButton("Удалить") { _, _ ->
                                alertDelete.create().show()
                            }
                            alert.create().show()
                        },
                        Modifier.padding(top = 0.dp, bottom = 10.dp),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(mainSecondColor)
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textReduces(it.value),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = mainTextColor
                            )
                            Column() {
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                                Text(
                                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RepeatReminderColumn(id: List<Int>, data: Map<Long, String>, remindersViewModel: ReminderViewModel = hiltViewModel()) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        remindersViewModel.deleteReminder(mapOf(it.key.toString() to it.value+".Повтор%#%:#%:"))
                        dateForVisitToVet.remove(it.key)
                        repeatReminderMap.remove(it.key)
                    }
                    alertDelete.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
//                        alert.setTitle(it.value)
                        alert.setMessage(it.value)
                        alert.setCancelable(true)
                        alert.setPositiveButton("Удалить") { _, _ ->
                            alertDelete.create().show()
                        }
                        alert.create().show()
                    }, shape = RoundedCornerShape(0.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(mainSecondColor)
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textReduces(it.value),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = mainTextColor
                            )
                            Column() {
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                                Text(
                                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DayReminderColumn(id: List<Int>, data: Map<Long, String>, remindersViewModel: ReminderViewModel = hiltViewModel()) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        remindersViewModel.deleteReminder(mapOf(it.key.toString() to it.value.toString()))
                        dateForVisitToVet.remove(it.key)
                        dayReminderMap.remove(it.key)
                    }
                    alertDelete.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
//                        alert.setTitle(it.value)
                        alert.setMessage(it.value)
                        alert.setCancelable(true)
                        alert.setPositiveButton("Удалить") { _, _ ->
                            alertDelete.create().show()
                        }
                        alert.create().show()
                    }, shape = RoundedCornerShape(0.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(mainSecondColor)
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textReduces(it.value),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = mainTextColor
                            )
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(Icons.Outlined.Notifications, contentDescription = "")
                            Column() {
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                                Text(
                                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                            }
//                            }
                        }
                    }
                }
            }
        }
    }

    private fun textReduces(text: String): String {
        var reducedText = ""
        reducedText = if (text.length > 18) {
            text.substring(0, 20) + "..."
        } else {
            text
        }
        return reducedText
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun WeeklyReminderColumn(id: List<Int>,data: Map<Long, String>, remindersViewModel: ReminderViewModel = hiltViewModel()) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        dateForVisitToVet.remove(it.key)
                        weeklyReminderMap.remove(it.key)
                        remindersViewModel.deleteReminder(mapOf(it.key.toString() to it.value.toString()))
                    }
                    alertDelete.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
//                        alert.setTitle(it.value)
                        alert.setMessage(it.value)
                        alert.setCancelable(true)
                        alert.setPositiveButton("Удалить") { _, _ ->
                            alertDelete.create().show()
                        }
                        alert.create().show()
                    }, shape = RoundedCornerShape(0.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(mainSecondColor)
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textReduces(it.value),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = mainTextColor
                            )
                            Column() {
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                                Text(
                                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                        .format(it.key).toString(), color = mainTextColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun PastReminderColumn(id: List<Int>,data: Map<Long, String>, remindersViewModel: ReminderViewModel = hiltViewModel()) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        dateForVisitToVet.remove(it.key)
                        pastReminderMap.remove(it.key)
                        remindersViewModel.deleteReminder(mapOf(it.key.toString() to it.value.toString()))
                    }
                    alertDelete.setNegativeButton("Нет") { dialog, _ ->
                        dialog.cancel()
                    }
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
//                        alert.setTitle(it.value)
                        alert.setMessage(it.value)
                        alert.setCancelable(true)
                        alert.setPositiveButton("Удалить") { _, _ ->
                            alertDelete.create().show()
                        }
                        alert.create().show()
                    }, shape = RoundedCornerShape(0.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(healthBarPastReminderColor)
                                .padding(start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textReduces(it.value),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = mainTextColor.copy(alpha = 0.5f)
                            )
                            Column() {
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.SHORT)
                                        .format(it.key).toString(),
                                    color = mainTextColor.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = DateFormat.getTimeInstance(DateFormat.SHORT)
                                        .format(it.key).toString(),
                                    color = mainTextColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Stats(times: MutableList<DogsInfoEntity>) {
        val res = remember { mutableStateOf((times[currentDogId.value].currentTimeWalk.toFloat() / (times[currentDogId.value].walkingTimeConst.toFloat())))}  //0.836
        var resReverse = remember { mutableStateOf(1F - res.value)}  // 0,164

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
        if ((resReverse.value*100).toInt()+(res.value*100).toInt() < 100) {
            if ((resReverse.value*100).toInt() != 0 && (resReverse.value*100).toInt()!= 100) {
                resReverse.value += 0.01F
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
                Column() {
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
                CircularProgressIndicator(progress = res.value, color = availableProgressColor, strokeWidth = 5.dp)
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
          composable(NavGraphRoutes.detailedDogs.route+"/{id}",
              arguments = listOf(navArgument("id"){
                type = NavType.IntType;defaultValue = 0;nullable = false
          })) {
//              val viewModel: DogsBreedEncyclopediaViewModel = hiltViewModel()
              DetailedDogScreen(navController = navController, it.arguments!!.getInt("id")/*, viewModel*/)
          }
          composable(NavGraphRoutes.health.route) {
              ReminderScreen(navController = navController)
          }
          composable(NavGraphRoutes.menu.route) {
              MenuScreen(navController = navController)
          }
          composable(NavGraphRoutes.addReminder.route) {
//              val reminderViewModel = hiltViewModel<ReminderViewModel>()
              AddReminderScreen(navController = navController, /*reminderViewModel*/)
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
      }
    }
    @Composable
    fun BottomNav(navController: NavController, tabs: Array<BottomTabs>) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: BottomTabs.HOME.route

        val routes = remember { BottomTabs.values().map { it.route }}
        if (currentRoute in routes) {
                BottomNavigation(backgroundColor = mainBackgroundColor) {
                    tabs.forEach { tab ->
                        BottomNavigationItem(icon = { Icon(painterResource(id = tab.icon), modifier = Modifier.size(25.dp),contentDescription = "", tint = mainTextColor)},
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
                            }, modifier = Modifier.navigationBarsPadding())
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
    }}
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListRowDogs(model: DogsBreedEncyclopediaEntity, navController: NavController) {
        Card(
            onClick = {
                navController.currentBackStackEntry?.arguments?.putInt("id", model.id)
                navController.navigate(NavGraphRoutes.detailedDogs.route+"/${model.id}")
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
                    if (!model.imageFile.isBlank()) {
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
    fun listDogs(navController: NavController) {
        LazyColumn {
            items(dogsEncyclopedia) { item ->
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
            Log.d("dogid", dogsProfileArray[0].currentTimeWalk.toString())
            Log.d("dogid", pagerState.currentPage.toString())
            currentDogId.value = pagerState.currentPage
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
                            GlideImage(imageModel = Uri.parse(list[it].image), modifier = Modifier
                                .padding(end = 10.dp)
                                .size(75.dp)
                                .clip(RoundedCornerShape(100)), contentScale = ContentScale.Crop)
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
        if (list.size >= 2 ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                HorizontalPagerIndicator(pagerState = pagerState, activeColor = mainTextColor)
            }
        }
    }

    private fun isScreenHorizontal(ctx: Context): Boolean {
        return ctx.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}


