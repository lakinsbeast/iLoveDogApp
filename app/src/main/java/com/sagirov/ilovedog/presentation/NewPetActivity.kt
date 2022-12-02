package com.sagirov.ilovedog.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.Activities.MainActivity.domain.model.DogsInfoEntity
import com.sagirov.ilovedog.domain.utils.PreferencesUtils
import com.sagirov.ilovedog.domain.utils.theme.mainBackgroundColor
import com.sagirov.ilovedog.domain.utils.theme.mainSecondColor
import com.sagirov.ilovedog.domain.utils.theme.mainTextColor
import com.sagirov.ilovedog.domain.utils.theme.textFieldUnFocusedIndicatorColor
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NewPetActivity : ComponentActivity() {
    private val PREF_NAME = "first_launch"

    @Inject
    lateinit var newPrefs: PreferencesUtils

    private var camera_uri: Uri? = null
    private var cameraUriPhoto = mutableStateOf("")
    private var cameraUriToUpdate = mutableStateOf("")

    private val dogsProfileArray = mutableStateListOf<DogsInfoEntity>()

    private val dogsInfoViewModel: DogsInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentID = mutableStateOf(intent.getIntExtra("id", -543253425))
        val petName =  mutableStateOf("")
        newPrefs = PreferencesUtils(this)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val dialogChooseActive = remember { mutableStateOf(false) }

            val petNameBreed = remember { mutableStateOf("") }
            var petAge = remember { mutableStateOf("") }
            var petAgeMonth = remember { mutableStateOf("") }
            val petPaddock = remember { mutableStateOf("") }
            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (cameraUriPhoto.value.isNotBlank() /*&& camera_uri != null*/) {
                    GlideImage(
                        Uri.parse(cameraUriPhoto.value),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                    )
                }
                if (dialogChooseActive.value) {
                    AlertDialog(
                        onDismissRequest = { dialogChooseActive.value = false },
                        buttons = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                OutlinedButton(
                                    onClick = {
                                        getImage.launch(arrayOf("image/*"))
                                        dialogChooseActive.value = false
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(),shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = "Галерея", color = mainTextColor)
                                }
                                OutlinedButton(
                                    onClick = {
                                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                            //permission was not enabled
                                            getTakeCameraPhotoPermissionRequest.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                        } else {
                                            //permission already granter
                                            try {
                                                openCamera()
                                            } catch(e: Exception) {
                                                Toast.makeText(this@NewPetActivity, "Не удается открыть камеру :(", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        dialogChooseActive.value = false
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(),shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = "Камера", color = mainTextColor)
                                }
                            }
                        })
                }
                OutlinedButton(onClick = {
                    dialogChooseActive.value = true
                },
                    Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp, top = 5.dp
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
                    Text("Фото", color = mainTextColor)
                }
                TextField(label = {
                    Text(
                        text = "Имя питомца:",
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                }, value = petName.value, onValueChange = { petName.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = mainTextColor,
                        focusedLabelColor = mainTextColor,
                        cursorColor = mainTextColor,
                        textColor = mainTextColor,
                        unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                    ), singleLine = true
                )
                // ПОКА ЧТО УДАЛИЛ, ИБО ЛИШНЕЕ, НАДО ПО-ДРУГОМУ ДОБАВЛЯТЬ ДАТУ РОЖДЕНИЯ!
//                Row() {
//                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
//                        value = petAge.value, onValueChange = {petAge.value = it}, label = {Text("Лет")}, modifier = Modifier.width((screenWidth / 2.79).dp))
//                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
//                        value = petAgeMonth.value, onValueChange = {petAgeMonth.value = it}, label = {Text("Месяцев")}, modifier = Modifier.width((screenWidth / 2.79).dp))
//                }
                TextField(placeholder = {
                    Text(
                        text = "Порода питомца:",
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                }, value = petNameBreed.value, onValueChange = { petNameBreed.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = mainTextColor,
                        focusedLabelColor = mainTextColor,
                        cursorColor = mainTextColor,
                        textColor = mainTextColor,
                        unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                    ), singleLine = true
                )
                TextField(placeholder = {
                    Text(
                        text = "Время выгула питомца?(минут):",
                        fontSize = 15.sp,
                        color = mainTextColor
                    )
                },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = mainTextColor,
                        focusedLabelColor = mainTextColor,
                        cursorColor = mainTextColor,
                        textColor = mainTextColor,
                        unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                    ),
                    value = petPaddock.value,
                    onValueChange = { petPaddock.value = it },
                    singleLine = true
                )
                OutlinedButton(
                    onClick = {
                        if ((/*petAge.value!= "" &&*/ petName.value != "" && petNameBreed.value != "" && petPaddock.value != "")) {
                            if (petPaddock.value.contains("[0-999]".toRegex()) && !petPaddock.value.contains(
                                    " "
                                )
                            ) {
                                petPaddock.value = (petPaddock.value.toLong() * 60000).toString()
                                newPrefs.putBoolean(PREF_NAME, "firstOpen", false)
                                lifecycleScope.launch {
                                    dogsInfoViewModel.insert(
                                        DogsInfoEntity(
                                            0,
                                            petName.value,
                                            Calendar.getInstance().time,
                                            petPaddock.value.toLong(),
                                            Calendar.getInstance().time,
                                            petNameBreed.value,
                                            "Сука",
                                            petPaddock.value.toLong(),
                                            56,
                                            cameraUriPhoto.value
                                        )
                                    )
                                }
                                startActivity(Intent(this@NewPetActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@NewPetActivity, "Неправильный формат времени выгула", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@NewPetActivity, "Заполните все поля", Toast.LENGTH_LONG).show()
                        } },
                    Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp, top = 20.dp
                        )
                        .height(70.dp)
                        .fillMaxWidth()
                        .border(
                            width = 0.dp, color = Color.Black,
                            shape = RoundedCornerShape(50)
                        )
                        .clip(RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = mainSecondColor, contentColor = Color.Black)) {
                    Text("Добавить питомца", color = mainTextColor)
                }
                if (intentID.value != -543253425) {
                    LaunchedEffect(rememberCoroutineScope()) {
                        dogsInfoViewModel.dogProfiles.collect {
                            if (it.isNotEmpty()) {
                                dogsProfileArray.clear()
                                dogsProfileArray.addAll(it)
                            }
                        }
//                        dogsInfoViewModel.getAllDogsProfiles.flowWithLifecycle(
//                            lifecycle,
//                            Lifecycle.State.STARTED
//                        ).onEach {
//                            if (it.isNotEmpty()) {
//                                dogsProfileArray.clear()
//                                dogsProfileArray.addAll(it)
//                            }
//                        }.launchIn(lifecycleScope)
                    }
//                    dogsInfoViewModel.getAllDogsProfiles.observe(this@NewPetActivity) {
//                        dogsProfileArray.clear()
//                        dogsProfileArray.addAll(it)
//                    }
                    OutlinedButton(
                        onClick = {
                            if (!cameraUriPhoto.value.isBlank()) {
                                cameraUriToUpdate.value = cameraUriPhoto.value
                            } else {
                                cameraUriToUpdate.value = dogsProfileArray[intentID.value].image
                            }
                            if ((petName.value != "" && petNameBreed.value != "" && petPaddock.value != "")) {
                                if (petPaddock.value.contains("[0-999]".toRegex()) && !petPaddock.value.contains(
                                        " "
                                    )
                                ) {
                                    dogsInfoViewModel.updateDogProfile(
                                        DogsInfoEntity(
                                            dogsProfileArray[intentID.value].id,
                                            petName.value,
                                            dogsProfileArray[intentID.value].dateBirth,
                                            (petPaddock.value.toLong() * 60000),
                                            dogsProfileArray[intentID.value].lastWalk,
                                            petNameBreed.value,
                                            dogsProfileArray[intentID.value].gender,
                                            (petPaddock.value.toLong() * 60000),
                                            dogsProfileArray[intentID.value].weight,
                                            cameraUriToUpdate.value
                                        )
                                    )
                                startActivity(Intent(this@NewPetActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@NewPetActivity, "Неправильный формат времени выгула", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@NewPetActivity, "Заполните все поля", Toast.LENGTH_LONG).show()
                        } },
                        Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp, top = 5.dp
                            )
                            .height(70.dp)
                            .fillMaxWidth()
                            .border(
                                width = 0.dp, color = Color.Black,
                                shape = RoundedCornerShape(50)
                            )
                            .clip(RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = mainSecondColor, contentColor = Color.Black)) {
                        Text("Обновить питомца", color = mainTextColor)
                    }
                    OutlinedButton(onClick = {
                        if (dogsProfileArray.size > 1) {
                            dogsInfoViewModel.deleteDogProfile(dogsProfileArray[intentID.value].id)
                            startActivity(Intent(this@NewPetActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@NewPetActivity, "Вы не можете удалить единственный профиль собаки", Toast.LENGTH_LONG).show()
                        }},
                        Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp, top = 5.dp
                            )
                            .height(70.dp)
                            .fillMaxWidth()
                            .border(
                                width = 0.dp, color = Color.Black,
                                shape = RoundedCornerShape(50)
                            )
                            .clip(RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = mainSecondColor, contentColor = Color.Black)) {
                        Text("Удалить питомца", color = mainTextColor)
                    }
                }


            }


        }
    }

    private val getDogPhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        cameraUriPhoto.value = camera_uri.toString()
    }
    private val getTakeCameraPhotoPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
        it.entries.forEach {
            if (it.value){
                try {
                    openCamera()
                } catch(e: Exception) {
                    Toast.makeText(this, "Нет доступа к камере", Toast.LENGTH_LONG).show()
                    Log.d("exception", e.toString())
                }
            } else {
//                Toast.makeText(this, "Вы не дали разрешение на камеру", Toast.LENGTH_LONG).show()
            }
        }
    }
    private val getImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            this@NewPetActivity.contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            var displayName = ""
            val cursor: Cursor? = contentResolver.query(
                it, null, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.d("name", "Display Name: $displayName")
            }
            cursor.close()
            cameraUriPhoto.value = it.toString()
            Log.d("image", it.toString())
        }
    }
    @Suppress("DEPRECATION")
    private fun openCamera() {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmSS").format(Date())
        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "NotesPhotos")
        storageDir.mkdir()
        val imageFile = File(storageDir, "image".plus(Calendar.getInstance().timeInMillis).plus(timeStamp).plus(".jpg"))
        imageFile.createNewFile()
        if (!imageFile.parentFile?.exists()!!) {
            imageFile.parentFile?.mkdirs()
        }
        if (!imageFile.exists()) {
            imageFile.mkdirs()
        }
        camera_uri = FileProvider.getUriForFile(this, "com.sagirov.ilovedog.presentation.NewPetActivity.provider", imageFile)
        lifecycleScope.launch(Dispatchers.Main) {
            getDogPhoto.launch(camera_uri)
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode) {
//            PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //perm from popup was granted
//                    openCamera()
//                } else {
//                    // perm from popup was denied
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
}
