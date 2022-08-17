package com.sagirov.ilovedog.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagirov.ilovedog.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.*
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.ViewModels.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.ViewModels.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch
import java.util.*

//TODO{НЕ ОБЯЗАТЕЛЬНО - Сделать как в Android Now синхронизацию данных в WorkManager'e }
//TODO{Сделать цветовую палитру и применить ее ко всем компонентам}
val selectedIndex =  mutableStateOf(0)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"
    private val PREF_NAME_PET = "mypets"
    private val PREF_NAME_DATES = "dates"
    lateinit var prefs: SharedPreferences

    private var isBack = true
    private var isDateChecked = false

    companion object {
        var myPetPaddockT = MutableLiveData(false)
    }

    private var badgeCount = mutableStateOf(0)
    private var isBadgeShown = mutableStateOf(false)

    private var dateForVisitToVet = mutableMapOf<Long, String>()
    private var pastReminderMap = mutableStateMapOf<Long, String>()
    private var weeklyReminderMap = mutableStateMapOf<Long, String>()
    private var otherReminderMapp = mutableStateMapOf<Long, String>()
    private var dayReminderMap = mutableStateMapOf<Long, String>()
    private var repeatReminderMap = mutableStateMapOf<Long, String>()
    private val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()
    private val dogsProfileArray = mutableStateListOf<DogsInfoEntity>()
    private val currentDogId = mutableStateOf(0)

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels {
        DogsInfoViewModelFactory((application as DogsApplication).DogsInfoRepo)
    }
    private val dogsBreedViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).DogsBreedEncyclopediaAppRepo)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CheckDarkMode.isDarkMode(true)


        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val frst_lnch = prefs.getBoolean("firstOpen", true)
        if (frst_lnch) {
            startActivity(Intent(this, FirstLaunchActivity::class.java))
            finish()
        }
        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
        val getArrayFromJson = prefs.getString("dateForVisitToVet", "")
        if (getArrayFromJson != "") {
            dateForVisitToVet = (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
            val it = dateForVisitToVet.iterator()
            while (it.hasNext()) {
                val item = it.next()
                when {
                    item.value.contains(".Повтор%#%:#%:")->repeatReminderMap[item.key] = item.value.substringBefore(".Повтор")
                    item.key < System.currentTimeMillis() -> pastReminderMap[item.key] = item.value
                    System.currentTimeMillis()+86400000 > item.key -> dayReminderMap[item.key] = item.value
                    System.currentTimeMillis()+604800000 > item.key -> weeklyReminderMap[item.key] = item.value
                    else -> otherReminderMapp[item.key] = item.value
                }
            }
        }

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
        dogsBreedViewModel.allDogs.observe(this) { list ->
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

        myPetPaddockT.observe(this) {
            try {
                if (dogsProfileArray.isNotEmpty()) {
                    dogsInfoViewModel.updateDogsTime(dogsProfileArray[currentDogId.value].id, currentTimeInMinutes)
                }
                Log.e("Exception", "updated")
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
            }
        }
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            systemUiController.setNavigationBarColor(Color(0xFFD0E0CC))
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            Scaffold(bottomBar = { BottomNav() }, scaffoldState = scaffoldState) {
                if (badgeCount.value > 0 && !isBadgeShown.value) {
                    isBadgeShown.value = true
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("У тебя ${badgeCount.value} истекающих напоминания")
                    }
                }
                if (selectedIndex.value == 0) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor)
                            .verticalScroll(rememberScrollState(), isScreenHorizontal(this))
                            .padding(start = 0.dp, end = 0.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp)) {
                            Box(Modifier.weight(0.5f)) {
                                Text(text = "Питомец", fontWeight = FontWeight.SemiBold, fontSize = 36.sp)
                            }
                        }
                        if (dogsProfileArray.isNotEmpty()) {
                            Dashboard(dogsProfileArray)
                        }
                        if (dogsProfileArray.isNotEmpty()) {
                            Stats(dogsProfileArray)
                        }
                        Column(Modifier.padding(start = 15.dp, end = 15.dp, top = 20.dp)) {
                            OutlinedButton(
                                onClick = {
                                    currentTimeInMinutes = dogsProfileArray[currentDogId.value].walkingTimeConst
                                    val intent = Intent(this@MainActivity, WalkLaunchActivity::class.java)
                                    intent.putExtra("id", currentDogId.value)
                                    startActivity(
                                        intent
                                    );
//                                    finish()
                                },
                                Modifier
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp
                                    )
                                    .height(70.dp)
                                    .fillMaxWidth()
                                ,shape = RoundedCornerShape(35),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = homeButtonColor,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("Прогулка!")
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
                if (selectedIndex.value == 1) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor)
                            .verticalScroll(rememberScrollState())
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            Box(Modifier.weight(0.5f)) {
                                Text(
                                    text = "Полезные статьи",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 36.sp
                                )
                            }
                        }
                        Card(onClick = { val intent = Intent(this@MainActivity, ArticleChoiceActivity::class.java); intent.putExtra("Article","feeding") ;startActivity(intent) }) {
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
                                        Text(text = "Кормление", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                        Card(onClick = { val intent = Intent(this@MainActivity, ArticleChoiceActivity::class.java); intent.putExtra("Article","activity") ;startActivity(intent) },
                            modifier = Modifier.padding(top = 10.dp)) {
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
                                        Text(text = "Активность", fontSize = 18.sp)

                                    }
                                }
                            }
                        }
                    }
                }
                if (selectedIndex.value == 2) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor)
                            .padding(bottom = 50.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp)) {
                            Box(Modifier.weight(0.5f)) {
                                Text(
                                    text = "Энциклопедия",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 36.sp
                                )
                            }
                        }
                        listDogs()
                    }
                }
                if (selectedIndex.value == 3) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor)
                            .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedButton(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        ReminderActivity::class.java
                                    )
                                )
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
                            Text("Добавить напоминание")
                        }
                        /*
                        * Я поменял внутри циклов items с item.value На item.key
                        * */
                        if (repeatReminderMap.isNotEmpty()) {
                            Text("Повторяющиеся напоминания:")
                            RepeatReminderColumn(data = repeatReminderMap)
                        }
                        if (dayReminderMap.isNotEmpty()) {
                            Text("Дневные напоминания:")
                            DayReminderColumn(data = dayReminderMap)
                        }
                        if (weeklyReminderMap.isNotEmpty()) {
                            Text("Недельные напоминания:")
                            WeeklyReminderColumn(data = weeklyReminderMap)
                        }
                        if (otherReminderMapp.isNotEmpty()) {
                            Text("Остальные напоминания:")
                            OtherReminderLazyColumn(data = otherReminderMapp)
                        }
                        if (pastReminderMap.isNotEmpty()) {
                            Text("Прошедшие напоминания:")
                            PastReminderColumn(data = pastReminderMap)
                        }

                    }
                }
                if (selectedIndex.value == 4) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(mainBackgroundColor)
                            .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(onClick = { startActivity(Intent(this@MainActivity, DocumentActivity::class.java)) }) {
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
                                        Text(text = "Документы/Анализы", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                        Card(onClick = { startActivity(Intent(this@MainActivity, VaccinationActivity::class.java)) }) {
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
                                        Text(text = "Вакцинации", fontSize = 18.sp)
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
                        Card(onClick = { startActivity(Intent(this@MainActivity, AboutAuthorPageActivity::class.java)) }) {
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
                                        Text(text = "Об авторе", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                        Card(onClick = { startActivity(Intent(this@MainActivity, DogNutritionActivity::class.java)) }) {
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
                                        Text(text = "Питание BARF", fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
    }



    override fun onBackPressed() {
        if (isBack && selectedIndex.value == 1) {
            selectedIndex.value = 3
        } else {
            super.onBackPressed()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun OtherReminderLazyColumn(data: Map<Long, String>) {
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
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
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
                    }, Modifier.padding(top = 0.dp, bottom = 10.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(mainSecondColor)
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(textReduces(it.value), fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
                            Column() {
                                Text(text = DateFormat.getDateInstance(DateFormat.SHORT).format(it.key).toString())
                                Text(text = DateFormat.getTimeInstance(DateFormat.SHORT).format(it.key).toString())
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RepeatReminderColumn(data: Map<Long, String>) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        dateForVisitToVet.remove(it.key)
                        repeatReminderMap.remove(it.key)
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
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
                    }) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(mainSecondColor)
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(textReduces(it.value), fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
                            Column() {
                                Text(text = DateFormat.getDateInstance(DateFormat.SHORT).format(it.key).toString())
                                Text(text = DateFormat.getTimeInstance(DateFormat.SHORT).format(it.key).toString())
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DayReminderColumn(data: Map<Long, String>) {
        LazyColumn {
            data.forEach {
                item(it.key) {
                    val alertDelete = android.app.AlertDialog.Builder(this@MainActivity)
                    alertDelete.setTitle("Удалить напоминание?")
                    alertDelete.setMessage("Вы уверены, что хотите удалить напоминание: \n${it.value}?")
                    alertDelete.setCancelable(true)
                    alertDelete.setPositiveButton("Да") { _, _ ->
                        dateForVisitToVet.remove(it.key)
                        dayReminderMap.remove(it.key)
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
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
                    }) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(mainSecondColor)
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(textReduces(it.value), fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(Icons.Outlined.Notifications, contentDescription = "")
                                Column() {
                                    Text(text = DateFormat.getDateInstance(DateFormat.SHORT).format(it.key).toString())
                                    Text(text = DateFormat.getTimeInstance(DateFormat.SHORT).format(it.key).toString())
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
    fun WeeklyReminderColumn(data: Map<Long, String>) {
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
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
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
                    }) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(mainSecondColor)
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(textReduces(it.value), fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
                            Column() {
                                Text(text = DateFormat.getDateInstance(DateFormat.SHORT).format(it.key).toString())
                                Text(text = DateFormat.getTimeInstance(DateFormat.SHORT).format(it.key).toString())
                            }
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun PastReminderColumn(data: Map<Long, String>) {
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
                        val json: String = Gson().toJson(dateForVisitToVet)
                        prefs.edit().putString("dateForVisitToVet", json).apply()
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
                    }) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(healthBarPastReminderColor)
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(textReduces(it.value), fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1, color = Color.Black.copy(alpha = 0.5f))
                            Column() {
                                Text(text = DateFormat.getDateInstance(DateFormat.SHORT).format(it.key).toString(), color = Color.Black.copy(alpha = 0.5f))
                                Text(text = DateFormat.getTimeInstance(DateFormat.SHORT).format(it.key).toString(), color = Color.Black.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Stats(times: MutableList<DogsInfoEntity>) {
        val res =  mutableStateOf((times[currentDogId.value].currentTimeWalk.toFloat() / (times[currentDogId.value].walkingTimeConst.toFloat())))  //0.836
        var resReverse =   mutableStateOf(1F - res.value)  // 0,164

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
        Text(text = "Статистика", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 15.dp, end = 15.dp))
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column() {
                    Text(text = "Сегодняшний план", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "${(resReverse.value*100).toInt()}% выполнено", color = Color.Gray)
                }
                CircularProgressIndicator(progress = resReverse.value, color = planProgressColor, strokeWidth = 5.dp)
            }
        }
        Card(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp), shape = RoundedCornerShape(10.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(mainSecondColor)
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(text = "Энергии доступно", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
//                    Text(text = res.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${(res.value*100).toInt()}% энергии", color = Color.Gray)
                }
                CircularProgressIndicator(progress = res.value, color = availableProgressColor, strokeWidth = 5.dp)
            }
        }
    }
    @Composable
    fun BottomNav() {
        BottomNavigation(backgroundColor = bottomNavBackgroundColor) {
            val home = R.drawable.home_48px
            val myPets = R.drawable.school_48px
            val myCard = R.drawable.menu_book_48px
            val health = R.drawable.health_and_safety_48px
            val menu = Icons.Outlined.Menu
            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(home), "", modifier = Modifier.size(25.dp))
            },
//            label = { Text(text = "", fontSize = 9.sp) }, //Питомец
                selected = (selectedIndex.value == 0),
                onClick = {
                    selectedIndex.value = 0
                })
            BottomNavigationItem(icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(myPets),
                    "",
                    modifier = Modifier.size(25.dp)
                )
            },
//            label = { Text(text = "", fontSize = 9.sp) },//Знания
                selected = (selectedIndex.value == 1),
                onClick = {
                    selectedIndex.value = 1
                })
            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(myCard), "", modifier = Modifier.size(25.dp))
            },
//            label = { Text(text = "", fontSize = 9.sp) },//Томик
                selected = (selectedIndex.value == 2),
                onClick = {
                    selectedIndex.value = 2
                })
            BottomNavigationItem(icon = {
                if (badgeCount.value > 0) {
                    BadgedBox(badge = { Text(text = badgeCount.value.toString(), modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(10.dp))
                        .padding(
                            when {
                                badgeCount.value < 10 -> 7.dp
                                badgeCount.value < 100 -> 5.dp
                                else -> 5.dp
                            }, 0.dp, when {
                                badgeCount.value < 10 -> 7.dp
                                badgeCount.value < 100 -> 5.dp
                                else -> 5.dp
                            }, 0.dp
                        ), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }) {
                        Icon(imageVector = ImageVector.vectorResource(health), "", modifier = Modifier.size(25.dp))
                    }
                } else { Icon(imageVector = ImageVector.vectorResource(health), "", modifier = Modifier.size(25.dp)) }
            },
//            label = { Text(text = "", fontSize = 9.sp) }, //Здоровье
                selected = (selectedIndex.value == 3),
                onClick = {
                    selectedIndex.value = 3
                })
            BottomNavigationItem(icon = {
                Icon(imageVector = menu, "", modifier = Modifier.size(25.dp))
            },
//                label = { Text(text = "", fontSize = 9.sp) }, //Меню
                selected = (selectedIndex.value == 4),
                onClick = {
                    selectedIndex.value = 4
                })
        }
    }
//    @Composable
//    fun FloatingActionButtonMainMenu() {
//        val ctx = LocalContext.current
//        FloatingActionButton(onClick = {
//            startActivity(
//                Intent(
//                    ctx,
//                    NewPetActivity::class.java
//                )
//            )
//        }, backgroundColor = Color.White, elevation = FloatingActionButtonDefaults.elevation(0.dp)) {
//            Icon(Icons.Filled.Add, contentDescription = "")
//        }
//    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListRowDogs(model: DogsBreedEncyclopediaEntity) {
        Card(onClick = {
            val intent = Intent(this@MainActivity, DetailedDogActivity::class.java)
            intent.putExtra("id", model.id)
            startActivity(intent)
        }, modifier = Modifier.padding(top = 10.dp))
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
                            overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold, fontSize = 16.sp
                        )
                        Text(
                            text = model.origin,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = model.lifeSpan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(
                    Modifier.padding(end = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = model.litterSize, color = Color.Black)
                }
            }
        }
    }
    @Composable
    fun listDogs() {
        LazyColumn {
            items(dogsEncyclopedia) { item ->
                ListRowDogs(item)
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    @Composable
    fun Dashboard(list: MutableList<DogsInfoEntity>) {
        val ctx = LocalContext.current
        var pagerState = rememberPagerState()
        Text(text = "Профиль", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 15.dp, end = 15.dp))

        HorizontalPager(count = list.size, state = pagerState) {
            currentDogId.value = pagerState.currentPage
            Card(onClick = { val intent = Intent(ctx, NewPetActivity::class.java)
                intent.putExtra("id", currentDogId.value)
                startActivity(intent)}, elevation = 15.dp,
                shape = RoundedCornerShape(15.dp), modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(mainSecondColor)
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
                            Text(text = list[it].breedName)
                            Text(text = list[it].name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "")
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
                HorizontalPagerIndicator(pagerState = pagerState)
            }
        }
    }

    private fun isScreenHorizontal(ctx: Context): Boolean {
        return ctx.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}


