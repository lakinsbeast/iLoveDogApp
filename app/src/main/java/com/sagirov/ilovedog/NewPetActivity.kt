package com.sagirov.ilovedog

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsInfoEntity
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

@AndroidEntryPoint
class NewPetActivity : ComponentActivity() {
    private val PREF_NAME_PET = "mypets"
    private lateinit var prefsMyPet: SharedPreferences
    private var camera_uri: Uri? = null
    private var cameraUriPhoto = mutableStateOf("")

    private val PERMISSION_CODE = 1000

    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefsMyPet = getSharedPreferences(PREF_NAME_PET, MODE_PRIVATE)
        val edit = prefsMyPet.edit()
        setContent {
            var petNameBreed = remember { mutableStateOf("")}
            var petName = remember { mutableStateOf("")}
            var petAge = remember { mutableStateOf("")}
            var petAgeMonth = remember { mutableStateOf("")}
            var petPaddock = remember { mutableStateOf("")}
            var petPaddockStandart = remember { mutableStateOf("")}
            val screenWidth = LocalConfiguration.current.screenWidthDp
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (cameraUriPhoto.value != ""){
                    Image(bitmap = MediaStore.Images.Media.getBitmap(this@NewPetActivity.getContentResolver(),camera_uri).asImageBitmap(),
                        "", contentScale = ContentScale.Crop, modifier = Modifier.clip(CircleShape).size(100.dp))
                }
                Button(onClick = {
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
                }){
                    Text("Открыть камеру")
                }
                TextField(label = { Text(text = "Имя питомца:", fontSize = 15.sp)}, value = petName.value, onValueChange = {petName.value = it},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                Row() {
                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                        value = petAge.value, onValueChange = {petAge.value = it}, label = {Text("Лет")}, modifier = Modifier.width((screenWidth / 2.79).dp))
                    TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                        value = petAgeMonth.value, onValueChange = {petAgeMonth.value = it}, label = {Text("Месяцев")}, modifier = Modifier.width((screenWidth / 2.79).dp))
                }
                TextField(placeholder = { Text(text = "Порода питомца:", fontSize = 15.sp)},value = petNameBreed.value, onValueChange = {petNameBreed.value = it},
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                TextField(placeholder = { Text(text = "Время выгула питомца?(минут):", fontSize = 15.sp)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent), value = petPaddock.value, onValueChange = {petPaddock.value = it})
                OutlinedButton(onClick = {
                    if ((petAge.value!= "" && petName.value != "" && petNameBreed.value != "" && petPaddock.value != "")) {
                        petPaddock.value.toLong()
                        petPaddock.value = (petPaddock.value.toLong()*60000).toString()
                        edit.putString("mypetName", petName.value)
                        edit.putString("mypetBreed", petNameBreed.value)
                        edit.putString("mypetAge", petAge.value)
                        edit.putString("mypetAgeMonth", petAgeMonth.value)
                        edit.putString("mypetPaddock", petPaddock.value)
                        edit.putString("mypetPaddockStandart", petPaddock.value)
                        edit.apply()

                        dogsViewModel.insertDogProfile(DogsInfoEntity(0,petName.value,Calendar.getInstance().time,
                            petPaddock.value.toLong(),Calendar.getInstance().time, petNameBreed.value,"Сука",
                            petPaddock.value.toLong(), 56,cameraUriPhoto.value))

                        startActivity(Intent(this@NewPetActivity, MainActivity::class.java))
                        finish()
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                    Text("Добавить питомца")
                }

            }


        }
    }

    val getDogPhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        cameraUriPhoto.value = camera_uri.toString()
    }
    val getTakeCameraPhotoPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        it.entries.forEach {
            if (it.value){
                try {
                    openCamera()
                } catch(e: Exception) {
                    Log.d("exception", e.toString())
                }
            } else {
//                Toast.makeText(this, "Нет доступа к камере", Toast.LENGTH_LONG).show()
            }
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
        camera_uri = FileProvider.getUriForFile(this, "com.sagirov.ilovedog.Activities.NewPetActivity.provider", imageFile)
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
