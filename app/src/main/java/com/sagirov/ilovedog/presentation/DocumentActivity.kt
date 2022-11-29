@file:Suppress("DEPRECATION")

package com.sagirov.ilovedog.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sagirov.ilovedog.domain.model.DocumentsEntity
import com.sagirov.ilovedog.R
import com.sagirov.ilovedog.domain.utils.theme.*
import com.sagirov.ilovedog.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

//TODO{ИЗМЕНИТЬ ВСЕ}
/**
непонятно как убрать брэкеты с после перевода с json в map в конвертере, поэтому приходится так изъявляться
чуть по-другому перепишу энтити, но это будет когда-нибудь)))))
 **/
@AndroidEntryPoint
class DocumentActivity : ComponentActivity() {
    private var newDocumentState = mutableStateOf(false)
    private var keyUriDocument = ""
    private var idDocument = mutableStateOf(0)

    private var allDocs = mutableStateListOf<DocumentsEntity>()
    private var AllDocsValues = mutableStateListOf<String>()
    private val newDocumentName = mutableStateOf("")
    private val isImageOpened = mutableStateOf(false)

    private val documentViewModel: DocumentViewModel by viewModels()

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            documentViewModel.documents.collect {
                allDocs.clear()
                AllDocsValues.clear()
                it.forEach {
                    allDocs.add(it)
                    AllDocsValues.add(it.docs.values.toString().replace("[", "").replace("]", ""))
                    Log.d("it", it.toString())
                }
            }
        }
        val getDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                this@DocumentActivity.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                var displayName = ""
                val cursor: Cursor? = contentResolver.query(
                    it, null, null, null, null, null
                )
                if (cursor!!.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    Log.d("name", "Display Name: $displayName")
                }
                cursor.close()
                Log.d("doc", "doc Uri: $it")
                Log.d("docName", File(it.path.toString()).name)
                Log.d("docName1", File(it.path.toString()).name.split(":")[0])
                documentViewModel.insert(
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
                    it, null, null, null, null, null
                )
                if (cursor!!.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    Log.d("name", "Display Name: $displayName")
                }
                cursor.close()

                documentViewModel.insert(
                    DocumentsEntity(
                        0,
                        mapOf(it.toString() to displayName)
                    )
                )
            }
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(mainBackgroundColor)
            val dialogState = remember { mutableStateOf(false) }
            val dialogChooseActive = remember { mutableStateOf(false) }


            Column(
                Modifier
                    .fillMaxSize()
                    .background(mainBackgroundColor)
            ) {
                if (isImageOpened.value) {
                    Box(Modifier.fillMaxSize()) {
                        IconButton(onClick = { isImageOpened.value = false }) {
                            GlideImage(
                                Uri.parse(keyUriDocument),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                if (dialogState.value) {
                    AlertDialog(
                        onDismissRequest = { dialogState.value = false },
                        buttons = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                OutlinedButton(
                                    onClick = {
                                        when {
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                                ":"
                                            )[0] == "document" -> {
                                                val intent = Intent(Intent.ACTION_VIEW)
                                                val typee = MimeTypeMap.getSingleton()
                                                    .getExtensionFromMimeType(
                                                        this@DocumentActivity.contentResolver.getType(
                                                            Uri.parse(keyUriDocument)
                                                        )
                                                    )
                                                when (typee) {
                                                    "doc" -> intent.setDataAndType(
                                                        Uri.parse(
                                                            keyUriDocument
                                                        ), "application/msword"
                                                    )
                                                    "pdf" -> intent.setDataAndType(
                                                        Uri.parse(
                                                            keyUriDocument
                                                        ), "application/pdf"
                                                    )
                                                    "odt" -> intent.setDataAndType(
                                                        Uri.parse(
                                                            keyUriDocument
                                                        ),
                                                        "application/vnd.oasis.opendocument.text"
                                                    )
                                                    "docx" -> intent.setDataAndType(
                                                        Uri.parse(
                                                            keyUriDocument
                                                        ),
                                                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                                    )
                                                }
                                                intent.flags =
                                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                try {
                                                    startActivity(intent)
                                                } catch (e: Exception) {
                                                    Log.d("error", e.toString())
                                                }
                                                dialogState.value = false
                                            }
                                            File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                                ":"
                                            )[0] == "image" -> {
                                                isImageOpened.value = true;dialogState.value =
                                                    false;dialogChooseActive.value = false
                                            }
                                        }
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(), shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    when {
                                        File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                            ":"
                                        )[0] == "document" ->
                                            Text(
                                                text = resources.getString(R.string.document_analyses_acitvity_dialog_open_doc), //Открыть документ
                                                color = mainTextColor
                                            )
                                        File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                            ":"
                                        )[0] == "image" ->
                                            Text(
                                                text = resources.getString(R.string.document_analyses_acitvity_dialog_open_photo),
                                                color = mainTextColor
                                            ) //Открыть фото
                                    }
                                }
                                OutlinedButton(
                                    onClick = {
                                        documentViewModel.delete(allDocs[idDocument.value].id /*allDocsIdsState[idDocument.value]*/)
                                        dialogState.value = false
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(), shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    when {
                                        File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                            ":"
                                        )[0] == "document" ->
                                            Text(
                                                text = resources.getString(R.string.document_analyses_acitvity_dialog_delete_doc), //Удалить документ
                                                color = mainTextColor
                                            )
                                        File(Uri.parse(keyUriDocument).path.toString()).name.split(
                                            ":"
                                        )[0] == "image" ->
                                            Text(
                                                text = resources.getString(R.string.document_analyses_acitvity_dialog_delete_photo),
                                                color = mainTextColor
                                            ) //Удалить фото
                                    }
//                                        Text(text = "Удалить документ")
                                }
                                OutlinedButton(
                                    onClick = {
                                        dialogState.value = false
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(), shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row() {
                                        TextField(
                                            label = {
                                                Text(
                                                    text = resources.getString(R.string.document_analyses_acitvity_dialog_new_name) + ":", //Название
                                                    fontSize = 15.sp,
                                                    color = mainTextColor
                                                )
                                            },
                                            value = newDocumentName.value,
                                            onValueChange = { newDocumentName.value = it },
                                            colors = TextFieldDefaults.textFieldColors(
                                                backgroundColor = Color.Transparent,
                                                focusedIndicatorColor = mainTextColor,
                                                focusedLabelColor = mainTextColor,
                                                cursorColor = mainTextColor,
                                                textColor = mainTextColor,
                                                unfocusedIndicatorColor = textFieldUnFocusedIndicatorColor
                                            )
                                        )
                                        IconButton(onClick = {
                                            if (newDocumentName.value.isNotEmpty() || newDocumentName.value.isNotBlank()) {
                                                documentViewModel.update(
                                                    allDocs[idDocument.value].id,
//                                                        allDocsIds[idDocument.value],
                                                    mapOf(keyUriDocument to newDocumentName.value)
                                                )
                                                dialogState.value = false
                                                newDocumentName.value = ""
                                            } else {
                                                Toast.makeText(
                                                    this@DocumentActivity,
                                                    "Не удалось обновить название",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Send,
                                                contentDescription = "", tint = mainTextColor
                                            )
                                        }
                                    }

                                }
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
                                        .fillMaxWidth(), shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = resources.getString(R.string.document_analyses_acitvity_dialog_gallery_text),
                                        color = mainTextColor
                                    ) //gallery
                                }
                                OutlinedButton(
                                    onClick = {
                                        getDocument.launch(
                                            arrayOf(
                                                "application/msword",
                                                "application/pdf",
                                                "application/vnd.oasis.opendocument.text",
                                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                            )
                                        )
                                        dialogChooseActive.value = false
                                    },
                                    Modifier
                                        .height(60.dp)
                                        .fillMaxWidth(), shape = RoundedCornerShape(0),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = mainSecondColor,
                                        contentColor = Color.Black
                                    ), contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = resources.getString(R.string.document_analyses_acitvity_dialog_document_text),
                                        color = mainTextColor
                                    ) //document
                                }
                            }
                        })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(mainBackgroundColor)
                        .padding(top = 10.dp), horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            dialogChooseActive.value = true
                        },
                        Modifier
                            .height(70.dp), shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = homeButtonColor,
                            contentColor = Color.Black
                        ), contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(Modifier.padding(start = 30.dp, end = 30.dp)) {
                            Text(
                                resources.getString(R.string.document_analyses_acitvity_button_text),
                                color = mainTextColor
                            ) //New docs/analyses
                        }
                    }
                }
                if (!allDocs.isEmpty()) {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                    ) {
                        itemsIndexed(allDocs /*allDocsKeys*/) { index, item ->
                            OutlinedButton(
                                onClick = {
                                    dialogState.value = true
                                    keyUriDocument =
                                        item.docs.keys.toString().replace("[", "").replace("]", "")
                                    idDocument.value = index
                                },
                                Modifier
                                    .height(60.dp)
                                    .fillMaxWidth(), shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = mainSecondColor,
                                    contentColor = Color.Black
                                ), contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    when {
                                        File(Uri.parse(item.docs.keys.toString()).path.toString()).name.split(
                                            ":"
                                        )[0] == "document" ->
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.documenticon48px),
                                                contentDescription = "", tint = mainTextColor
                                            )
                                        File(Uri.parse(item.docs.keys.toString()).path.toString()).name.split(
                                            ":"
                                        )[0] == "image" ->
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.imageicon48px),
                                                contentDescription = "", tint = mainTextColor
                                            )
                                    }
                                    Text(
                                        text = (AllDocsValues[index] /*allDocsValues[index]*/),
                                        color = mainTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }

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







