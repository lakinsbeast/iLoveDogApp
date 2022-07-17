@file:Suppress("DEPRECATION")

package com.sagirov.ilovedog

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DocumentsEntity
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsApplication
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModel
import com.sagirov.ilovedog.DogsEncyclopediaDatabase.DogsBreedEncyclopediaViewModelFactory
import com.sagirov.ilovedog.ui.theme.ILoveDogTheme
import java.io.File
// TODO{*Сделать выбор расширение документа e.g. DOC, DOCX, PDF etc*}

class DocumentActivity : ComponentActivity() {

    private val allDocs = mutableStateMapOf<String, String>()
    private var allDocsTest = mutableMapOf<String, String>()
    private var newDocumentState = mutableStateOf(false)
    var documentName = ""
    var idDocument = ""

    private val dogsViewModel: DogsBreedEncyclopediaViewModel by viewModels {
        DogsBreedEncyclopediaViewModelFactory((application as DogsApplication).repo)
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogsViewModel.getAllDocuments.observe(this) {
            it.forEach {
                allDocsTest.put(
                    Uri.parse(it.docs.keys.toString()).toString().replace("[", "").replace("]", ""),
                    Uri.parse(it.docs.values.toString()).toString().replace("[", "").replace("]", "")
                )
            }
            allDocs.putAll(allDocsTest)
            Log.d("alldocs", allDocsTest.toString())
        }
        val getDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            this@DocumentActivity.contentResolver.takePersistableUriPermission(
                it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            dogsViewModel.insertDocumentFile(
                DocumentsEntity(
                    0,
                    mapOf(it.toString() to documentName)
                )
            )
        }

        setContent {
            val dialogState = remember { mutableStateOf(false) }

            if (!newDocumentState.value) {
                Column(Modifier.fillMaxSize()) {
                    if (dialogState.value) {
                        AlertDialog(onDismissRequest = { dialogState.value = false },
//                            title = { Text("Действие") },
//                            text = {Text("Выберите действие")},
                            buttons = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    OutlinedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW)
                                            Log.d("idDoc", idDocument)
                                            intent.setDataAndType(Uri.parse(idDocument), "application/pdf")
                                            intent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                            try {
                                                startActivity(intent)
                                            } catch (e: Exception) {
                                                Log.d("error", e.toString())
                                            }
                                            dialogState.value = false
                                        },
                                        Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                        ,shape = RoundedCornerShape(0),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.White,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(text = "Открыть документ")
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
                                            backgroundColor = Color.White,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(text = "Удалить документ")
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
                                            backgroundColor = Color.White,
                                            contentColor = Color.Black
                                        ), contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(text = "Изменить название документа")
                                    }

                                }
                            })
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.Center) {
                        OutlinedButton(
                            onClick = {
                                newDocumentState.value = true
                            },
                            Modifier
//                            .padding(
//                                start = 50.dp,
//                                end = 50.dp
//                            )
                                .height(70.dp)
//                            .fillMaxWidth()
                            ,shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White,
                                contentColor = Color.Black
                            ), contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(Modifier.padding(start = 30.dp, end = 30.dp)){
                                Text("Новый документ!")
                            }
                        }
                    }
                    LazyColumn(Modifier.fillMaxSize().padding(top = 20.dp)) {
                        allDocs.forEach { (key, value) ->
                            item(key) {
                                OutlinedButton(
                                    onClick = {
                                        dialogState.value = true
                                        idDocument = key
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth()
                                    ,shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.White,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = (value))
                                }
//                                Button(
//                                    onClick = {
//                                        dialogState.value = true
//                                              idDocument = key
////                                        startActivity(
////                                            Intent(Intent.ACTION_VIEW).setDataAndType(
////                                                Uri.parse(
////                                                    key
////                                                ), "application/pdf"
////                                            )
////                                        )
//                                    },
//                                ) {
//                                    Text(text = (value))
//                                }
                            }
                        }
                    }
                }
            }
            else {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    val docName = rememberSaveable { mutableStateOf("")}
                    val isBtnEnabled = remember { mutableStateOf(false)}
                    TextField(label = { Text(text = "Название документа:", fontSize = 15.sp)}, value = docName.value, onValueChange = {docName.value = it
                        documentName = docName.value
                        isBtnEnabled.value = it.isNotEmpty()
                    },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent))
                    Text(text = documentName)
                    Button(onClick = {
                        getDocument.launch(arrayOf("application/*"))
                        newDocumentState.value = false
                    }, enabled = isBtnEnabled.value) {
                        Text("Добавить документ")
                    }
                }
            }

        }
    }


    override fun onBackPressed() {
        if (!newDocumentState.value) {
            super.onBackPressed()
        } else {
            newDocumentState.value = false
        }
    }
//    @Composable
//    fun documentList(photos: MutableList<Uri>) {
//        val ctx = LocalContext.current
//        Text("wtf1")
//        LazyHorizontalGrid(
//            rows = GridCells.Adaptive(minSize = 32.dp),
//            contentPadding = PaddingValues(8.dp)
//        ) {
//            items(photos) {
////            MediaStore.Images.Media.getBitmap(ctx.contentResolver, it).asImageBitmap()
//                Button(onClick = {
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.setDataAndType(it, "application/*")
//
//                }) {
//                    Text(text = it.toString())
//                }
////            Image(painterResource(R.drawable.dog_first), contentDescription = "")
//            }
//        }
//    }
}







