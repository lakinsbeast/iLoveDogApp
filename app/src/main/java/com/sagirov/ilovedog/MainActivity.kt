package com.sagirov.ilovedog

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DateFormat
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.DogsKnowledgeBaseDatabase.*


val selectedIndex =  mutableStateOf(0)
class MainActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"
    private val PREF_NAME_PET = "mypets"
    private val PREF_NAME_DATES = "dates"
    private lateinit var prefs: SharedPreferences
//    private lateinit var prefsMyPet: SharedPreferences

    private var isBack = true

    private var dateForVisitToVet = mutableMapOf<Long, String>()
    private var weeklyReminderMap = mutableStateMapOf<Long, String>()
    private var otherReminderMapp = mutableStateMapOf<Long, String>()
    private val dogsEncyclopedia = mutableListOf<DogsBreedEncyclopediaEntity>()
    private val knowData = mutableListOf<DogsKnowledgeBaseEntity>()


    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }
    private val knowViewModel: DogsKnowledgeViewModel by viewModels {
        DogsKnowledgeBaseViewModelFactory((application as DogsApplication).repoKnow)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val frst_lnch = prefs.getBoolean("firstOpen", true)
        if (frst_lnch) {
            startActivity(Intent(this, FirstLaunchActivity::class.java))
            finish()
        }
        prefs = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val myPetName = prefs.getString("mypetName", "")
        val myPetNameBreed = prefs.getString("mypetBreed", "Нажмите сюда, чтоб добавить питомца")
        val myPetAge = prefs.getString("mypetAge", "")
        val myPetPaddock = prefs.getString("mypetPaddock", "")
        val myPetPaddockStandart = prefs.getString("mypetPaddockStandart", "")

        prefs = getSharedPreferences(PREF_NAME_DATES, MODE_PRIVATE)
        val getArrayFromJson = prefs.getString("dateForVisitToVet", "")
        if (getArrayFromJson != "") {
            dateForVisitToVet = (Gson().fromJson(getArrayFromJson, object : TypeToken<Map<Long, String>>() {}.type))
            val it = dateForVisitToVet.iterator()
            while (it.hasNext()) {
                val item = it.next()
                if (item.key < System.currentTimeMillis()) {
                    it.remove()
                    val json: String = Gson().toJson(dateForVisitToVet)
                    prefs.edit().putString("dateForVisitToVet", json).apply()
                }
                if (System.currentTimeMillis()+604800000 > item.key) {
                    weeklyReminderMap[item.key] = item.value
                } else {
                    otherReminderMapp[item.key] = item.value
                }
            }
        }



        dogsViewModel.allDogs.observe(this) { list ->
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
        knowViewModel.allKnowledge.observe(this) {
            knowData.addAll(it)
            Log.d("knowData", it.toString())
        }

        setContent {
            Scaffold(bottomBar = { BottomNav() }) {
                if (selectedIndex.value == 0) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState(), true)
                            .fillMaxSize()
                            .padding(start = 15.dp, end = 15.dp, bottom = 50.dp)
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            Box(Modifier.weight(0.5f)) {
                                Text(text = "Home", fontWeight = FontWeight.Bold, fontSize = 36.sp)
                            }
                        }
                        Dashboard(myPetName!!, myPetNameBreed!!, myPetAge!!, myPetPaddock!!,myPetPaddockStandart!!)
                        Stats(myPetPaddock, myPetPaddockStandart)
                        Column(Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                            OutlinedButton(
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            WalkLaunchActivity::class.java
                                        )
                                    )
                                    finish()
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
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("Прогулка!")
                            }
                            OutlinedButton(
                                onClick = {

                                },
                                Modifier
                                    .padding(
                                        start = 20.dp, top = 20.dp,
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
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("Достижения")
                            }
                        }
                    }
                }
                if (selectedIndex.value == 1) {
                    Column(
                        Modifier
                            .fillMaxSize()
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
//                                    Image(
//                                        painterResource(R.drawable.dog_first),
//                                        contentDescription = "",
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier
//                                            .padding(end = 10.dp)
//                                            .size(75.dp)
//                                            .clip(RoundedCornerShape(100))
//                                    )
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text(text = "Кормление", fontSize = 18.sp)

                                    }
                                }
                            }
                        }
                        Card(onClick = { val intent = Intent(this@MainActivity, ArticleChoiceActivity::class.java); intent.putExtra("Article","activity") ;startActivity(intent) }) {
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
//                                    Image(
//                                        painterResource(R.drawable.dog_first),
//                                        contentDescription = "",
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier
//                                            .padding(end = 10.dp)
//                                            .size(75.dp)
//                                            .clip(RoundedCornerShape(100))
//                                    )
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text(text = "Активность", fontSize = 18.sp)

                                    }
                                }
                            }
                        }
//                        HtmlText(textId = R.string.dog_feeding_text)


//                        DashboardPets(myPetName!!, myPetNameBreed!!, myPetAge!!, myPetPaddock!!, myPetPaddockStandart!!)
                    }
                }
                if (selectedIndex.value == 2) {
                    Column(
                        Modifier
                            .fillMaxSize()
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
                                backgroundColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Добавить напоминание!")
                        }
                        if (weeklyReminderMap.isNotEmpty()) {
                            Text("Напоминания на неделю:")
                            weeklyReminderColumn(data = weeklyReminderMap)
                        }
                        if (otherReminderMapp.isNotEmpty()) {
                            Text("Остальные напоминания:")
                            otherReminderLazyColumn(data = otherReminderMapp)
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
    fun otherReminderLazyColumn(data: Map<Long, String>) {
        LazyColumn {
            data.forEach {
                item(it.value) {
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Удалить напоминание?")
                        alert.setMessage("Вы уверены, что хотите удалить напоминание ${it.value}?")
                        alert.setCancelable(true)
                        alert.setPositiveButton(android.R.string.ok) { dialog, which ->
                            dateForVisitToVet.remove(it.key)
                            otherReminderMapp.remove(it.key)
                            val json: String = Gson().toJson(dateForVisitToVet)
                            prefs.edit().putString("dateForVisitToVet", json).apply()
                        }
                        alert.create().show()
                    }, Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(it.value, fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
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
    fun weeklyReminderColumn(data: Map<Long, String>) {
        LazyColumn {
            data.forEach {
                item(it.value) {
                    Card(onClick = {
                        val alert = android.app.AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Удалить напоминание?")
                        alert.setMessage("Вы уверены, что хотите удалить напоминание ${it.value}?")
                        alert.setCancelable(true)
                        alert.setPositiveButton(android.R.string.ok) { dialog, which ->
                            dateForVisitToVet.remove(it.key)
                            weeklyReminderMap.remove(it.key)
                            val json: String = Gson().toJson(dateForVisitToVet)
                            prefs.edit().putString("dateForVisitToVet", json).apply()
                        }
                        alert.create().show()
                    }) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(it.value, fontWeight = FontWeight.Bold, fontSize = 18.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
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
    fun Stats(_time: String, _timeStandart: String) {
        val time = _time.toLong()
        val timeStandart = _timeStandart.toLong()
        var res = (time.toFloat() / (timeStandart)) //0.836
        val resReverse = 1F-res // 0,164
        Text(text = "Stats", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card(onClick = { /*TODO*/ }) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column() {
                    Text(text = "Сегодняшний план", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${(resReverse*100).toInt()}% выполнено", color = Color.Gray)
                }
                CircularProgressIndicator(progress = resReverse, color = Color.Yellow, strokeWidth = 5.dp)
            }
        }
        Card(onClick = { /*TODO*/ }) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(text = "Энергии доступно", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${(res*100).toInt()}% энергии", color = Color.Gray)
                }
                CircularProgressIndicator(progress = res, color = Color.Yellow, strokeWidth = 5.dp)
            }
        }
//        Card(onClick = { /*TODO*/ }) {
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = 30.dp, bottom = 30.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Column() {
//                    Text(text = "Weekly objectives", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                    Text(text = "2 walks left", color = Color.Gray)
//                }
//                CircularProgressIndicator(progress = res, color = Color.Yellow, strokeWidth = 5.dp)
//            }
//        }
    }

    @Composable
    fun DashboardPets(name: String, breed: String, age: String, paddock: String, standartPaddock: String) {
        Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.dog_first),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(75.dp)
                            .clip(RoundedCornerShape(100))
                    )
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = breed)
                        Text(text = name)
                        if (age.isEmpty()) {
                            Text(text ="")
                        } else {
                            Text(text = "$age лет")
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (paddock.isNotEmpty() && standartPaddock.isNotEmpty()) {
                        when {
                            paddock == standartPaddock -> Text((standartPaddock.toLong()/60000).toString(), color = Color.Blue)
                            paddock < standartPaddock -> Text((paddock.toLong()/60000).toString(), color = Color.Blue)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun BottomNav() {
        BottomNavigation(backgroundColor = Color.White) {
            val home = R.drawable.home_48px
            val myPets = R.drawable.school_48px
            val myCard = R.drawable.menu_book_48px
            val health = R.drawable.health_and_safety_48px
            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(home), "", modifier = Modifier.size(25.dp))
            },
            label = { Text(text = "Меню") },
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
            label = { Text(text = "Знания") },
                selected = (selectedIndex.value == 1),
                onClick = {
                    selectedIndex.value = 1
                })
//            if (selectedIndex.value == 1) {
//                FloatingActionButtonMainMenu()
//            }
            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(myCard), "", modifier = Modifier.size(25.dp))
            },
            label = { Text(text = "Томик") },
                selected = (selectedIndex.value == 2),
                onClick = {
                    selectedIndex.value = 2
                })
            BottomNavigationItem(icon = {
                Icon(imageVector = ImageVector.vectorResource(health), "", modifier = Modifier.size(25.dp))
            },
            label = { Text(text = "Здоровье") },
                selected = (selectedIndex.value == 3),
                onClick = {
                    selectedIndex.value = 3
                })
        }
    }
    @Composable
    fun FloatingActionButtonMainMenu() {
        val ctx = LocalContext.current
        FloatingActionButton(onClick = {
            startActivity(
                Intent(
                    ctx,
                    NewPetActivity::class.java
                )
            )
        }, backgroundColor = Color.White, elevation = FloatingActionButtonDefaults.elevation(0.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "")
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListRowDogs(model: DogsBreedEncyclopediaEntity) {
        Card(onClick = {
            val intent = Intent(this@MainActivity, DetailedDogActivity::class.java)
            intent.putExtra("id", model.id)
            startActivity(intent)
        })
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(
                            resources.getIdentifier(model.imageFile, "drawable", packageName)
                        ), contentDescription = "", contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(75.dp)
                            .clip(RoundedCornerShape(100))
                    )
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = model.breedName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Dashboard(name: String, breed: String, age: String, paddock: String, standartPaddock: String) {
        val ctx = LocalContext.current
        Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card(onClick = { if (name.isEmpty()) {startActivity(Intent(ctx, NewPetActivity::class.java))} else {
            selectedIndex.value = 1
        }}) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.dog_first),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(75.dp)
                            .clip(RoundedCornerShape(100))
                    )
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = breed)
                        Text(text = name)
                        if (age.isEmpty()) {
                            Text(text ="")
                        } else {
                            Text(text = "$age лет")
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (paddock.isNotEmpty() && standartPaddock.isNotEmpty()) {
                        Log.d("paddock", paddock)
                        Log.d("standartPaddock", standartPaddock)
                        when {
                            paddock == standartPaddock -> Text((standartPaddock.toLong()/60000).toString(), color = Color.Blue)
                            paddock < standartPaddock -> Text((paddock.toLong()/60000).toString(), color = Color.Blue)
                        }
                    }
                }
            }
        }
    }
}


