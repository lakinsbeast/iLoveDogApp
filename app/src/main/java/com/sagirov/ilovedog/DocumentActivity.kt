@file:Suppress("DEPRECATION")

package com.sagirov.ilovedog

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.ui.theme.homeButtonColor
import com.sagirov.ilovedog.ui.theme.mainBackgroundColor
import com.sagirov.ilovedog.ui.theme.mainSecondColor
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DocumentActivity : ComponentActivity() {
    private val allDocsKeys = mutableStateListOf<String>()
    private val allDocsValues = mutableStateListOf<String>()
    private var allDocsIds = mutableListOf<Int>()
    private var allDocsIdsState = mutableStateListOf<Int>()
    private var newDocumentState = mutableStateOf(false)
    private var keyUriDocument = ""
    private var idDocument = mutableStateOf(0)

    private val newDocumentName = mutableStateOf("")

    private val isImageOpened = mutableStateOf(false)

    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogsViewModel.getAllDocuments.observe(this) {
            allDocsIds.clear()
            allDocsIdsState.clear()
            allDocsKeys.clear()
            allDocsValues.clear()
            it.forEach {
                allDocsIds.add(it.id)
                allDocsKeys.add(Uri.parse(it.docs.keys.toString()).toString().replace("[", "").replace("]", ""))
                allDocsValues.add(Uri.parse(it.docs.values.toString()).toString().replace("[", "").replace("]", ""))
            }
            allDocsIdsState.addAll(allDocsIds)


        }
        val getDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                this@DocumentActivity.contentResolver.takePersistableUriPermission(
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
                Log.d("doc", "doc Uri: $it")
                Log.d("docName", File(it.path.toString()).name)
                Log.d("docName1", File(it.path.toString()).name.split(":")[0])
                dogsViewModel.insertDocumentFile(
                    DocumentsEntity(
                        0,
                        mapOf(it.toString() to displayName)
                    )
                )
            }
        }

        val getImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                this@DocumentActivity.contentResolver.takePersistableUriPermission(
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

                dogsViewModel.insertDocumentFile(
                    DocumentsEntity(
                        0,
                        mapOf(it.toString() to displayName)
                    )
                )
            }
        }

        setContent {
//            val systemUiController: SystemUiController = rememberSystemUiController()
            val dialogState = remember { mutableStateOf(false) }
            val dialogChooseActive = remember { mutableStateOf(false) }

                Column(Modifier.fillMaxSize()) {
                    if (isImageOpened.value) {
                        Box(Modifier.fillMaxSize()) {
                            IconButton(onClick = { isImageOpened.value = false }) {
                                GlideImage(Uri.parse(keyUriDocument), contentScale = ContentScale.Fit)
                            }
                        }
                    } else {
                    }
                    if (dialogState.value) {
                        AlertDialog(onDismissRequest = { dialogState.value = false },
                            buttons = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    OutlinedButton(
                                        onClick = {
                                            when {
                                                File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "document" ->
                                                    {val intent = Intent(Intent.ACTION_VIEW)
                                                    val typee = MimeTypeMap.getSingleton().getExtensionFromMimeType(this@DocumentActivity.contentResolver.getType(Uri.parse(keyUriDocument)))
                                                    when (typee) {
                                                        "doc" -> intent.setDataAndType(Uri.parse(keyUriDocument), "application/msword")
                                                        "pdf" -> intent.setDataAndType(Uri.parse(keyUriDocument), "application/pdf")
                                                        "odt" -> intent.setDataAndType(Uri.parse(keyUriDocument), "application/vnd.oasis.opendocument.text")
                                                        "docx" -> intent.setDataAndType(Uri.parse(keyUriDocument), "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                                                    }
                                                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                    try {
                                                        startActivity(intent)
                                                    } catch (e: Exception) {
                                                        Log.d("error", e.toString())
                                                    }
                                                    dialogState.value = false}
                                                File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "image" ->
                                                    {
                                                        isImageOpened.value = true;dialogState.value = false;dialogChooseActive.value = false

                                                    }
                                            }
                                                  },
                                        Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                        ,shape = RoundedCornerShape(0),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = mainSecondColor,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        when {
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "document" ->
                                                Text(text = "Открыть документ")
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "image" ->
                                                Text(text = "Открыть фото")
                                        }
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            dogsViewModel.deleteDocumentFile(allDocsIdsState[idDocument.value])
                                            dialogState.value = false
                                        },
                                        Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                        ,shape = RoundedCornerShape(0),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = mainSecondColor,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        when {
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "document" ->
                                                Text(text = "Удалить документ")
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "image" ->
                                                Text(text = "Удалить фото")
                                        }
//                                        Text(text = "Удалить документ")
                                    }
                                    OutlinedButton(
                                        onClick = {

                                            dialogState.value = false
                                        },
                                        Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                        ,shape = RoundedCornerShape(0),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = mainSecondColor,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Row() {
                                            TextField(label = { Text(text = "Название:", fontSize = 15.sp)}, value = newDocumentName.value, onValueChange = {newDocumentName.value = it},
                                                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                                            IconButton(onClick = {
                                                if (newDocumentName.value.isNotEmpty() || newDocumentName.value.isNotBlank()) {
                                                    dogsViewModel.updateDocumentFile(allDocsIds[idDocument.value], mapOf(keyUriDocument to newDocumentName.value))
                                                    dialogState.value = false
                                                    newDocumentName.value = ""
                                                } else {
                                                    Toast.makeText(this@DocumentActivity, "Не удалось обновить название", Toast.LENGTH_SHORT).show()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Send,
                                                    contentDescription = ""
                                                )
                                            }
                                        }

//                                        when {
//                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "document" ->
//                                                Text(text = "Удалить документ")
//                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "image" ->
//                                                Text(text = "Удалить фото")
//                                        }
                                    }


//                                    OutlinedButton(
//                                        onClick = {
////                                            dogsViewModel.deleteDocumentFile(allDocsIdsState[idDocument.value])
//                                            dialogState.value = false
//                                        },
//                                        Modifier
//                                            .height(60.dp)
//                                            .fillMaxWidth()
//                                        ,shape = RoundedCornerShape(0),
//                                        colors = ButtonDefaults.buttonColors(
//                                            backgroundColor = mainSecondColor,
//                                            contentColor = Color.Black
//                                        ), contentPadding = PaddingValues(0.dp)
//                                    ) {
//                                        when {
//                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "document" ->
//                                                Text(text = "Изменить имя документа")
//                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(":")[0] == "image" ->
//                                                Text(text = "Изменить имя фото")
//                                        }
////                                        Text(text = "Удалить документ")
//                                    }
                                }
                            })
                    }
                    if (dialogChooseActive.value) {
                            AlertDialog(onDismissRequest = { dialogChooseActive.value = false },
                                buttons = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        OutlinedButton(
                                            onClick = {
                                                getImage.launch(arrayOf("image/*"))
                                                dialogChooseActive.value = false
                                            },
                                            Modifier
                                                .height(60.dp)
                                                .fillMaxWidth()
                                            ,shape = RoundedCornerShape(0),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = mainSecondColor,
                                                contentColor = Color.Black
                                            ), contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text(text = "Галерея")
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                getDocument.launch(arrayOf("application/msword","application/pdf","application/vnd.oasis.opendocument.text",
                                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                                                dialogChooseActive.value = false
                                            },
                                            Modifier
                                                .height(60.dp)
                                                .fillMaxWidth()
                                            ,shape = RoundedCornerShape(0),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = mainSecondColor,
                                                contentColor = Color.Black
                                            ), contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text(text = "Документ")
                                        }
                                    }
                                })
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
//                        .background(mainBackgroundColor)
                        .padding(top = 10.dp), horizontalArrangement = Arrangement.Center) {
                        OutlinedButton(
                            onClick = {
                                dialogChooseActive.value = true

                            },
                            Modifier
                                .height(70.dp)
                            ,shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = homeButtonColor,
                                contentColor = Color.Black
                            ), contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(Modifier.padding(start = 30.dp, end = 30.dp)){
                                Text("Новый документ/анализ!")
                            }
                        }
                    }
                    if (allDocsKeys.isEmpty()) {

                    }
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)) {
                        itemsIndexed(allDocsKeys) { index, item ->
                            OutlinedButton(
                                onClick = {
                                    dialogState.value = true
                                    keyUriDocument = item
                                    idDocument.value = index
                                },
                                Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                                ,shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = mainSecondColor,
                                    contentColor = Color.Black
                                ), contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    when {
                                        File(Uri.parse(item).path.toString()).name.split(":")[0] == "document" ->
                                            Icon(imageVector = ImageVector.vectorResource(R.drawable.documenticon48px), contentDescription = "")
                                        File(Uri.parse(item).path.toString()).name.split(":")[0] == "image" ->
                                            Icon(imageVector = ImageVector.vectorResource(R.drawable.imageicon48px), contentDescription = "")
                                    }
                                    Text(text = (allDocsValues[index]))
                                }
                            }
                        }
            }}

        }
    }
//    fun <T> SnapshotStateMap<T,T>.swapMap(newMap: MutableMap<T,T>){
//        clear()
//        putAll(newMap)
//    }



    override fun onBackPressed() {
        if (isImageOpened.value == true) {
            isImageOpened.value = false
            return
        }
        if (!newDocumentState.value) {
            super.onBackPressed()
        } else {
            newDocumentState.value = false
        }
    }
}







