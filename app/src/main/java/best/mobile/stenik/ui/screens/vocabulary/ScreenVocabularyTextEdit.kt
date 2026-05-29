package best.mobile.stenik.ui.screens.vocabulary

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import best.mobile.entities.Utils.TAG_STENIK

import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.vocabulary.custom_view.DropDownVocabularyText
import best.mobile.stenik.ui.screens.vocabulary.custom_view.TextFieldForVocabulary

import org.koin.androidx.compose.koinViewModel
import kotlin.String

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ScreenVocabularyTextEdit(
    viewModelVocabularyTextEdit: ViewModelVocabularyTextEdit = koinViewModel(),
    jsonStringArray: Array<String>,
    onClickBack:() -> Unit
) {
    //первичная загрузка списка слов
    LaunchedEffect(Unit) {
        viewModelVocabularyTextEdit.initDataForEdit(jsonStringArray)
        //Log.d(TAG_STENIK,"LaunchedEffect")
    }


    val bottomPadding = remember { mutableStateOf(0.dp) }

    val textStENikList = remember { viewModelVocabularyTextEdit.textStENikListForEditScreen }
    val vocabulary = remember {viewModelVocabularyTextEdit.vocabulary}

    val iconIsEnable = remember { viewModelVocabularyTextEdit.iconIsEnable }

    //Лаунчер для запроса разрешения микрофона
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG_STENIK, "GRANTED ")
        } else {
            Log.d(TAG_STENIK, "DENIED ")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Scaffold(
            bottomBar = {
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                {
                    ButtonStENik(stringResource(R.string.btn_save)) {
                        viewModelVocabularyTextEdit.saveTextStENikListInDataBase(){
                            onClickBack()
                        }

                    }
                }
            }
        ) { paddingValues ->
            bottomPadding.value = paddingValues.calculateBottomPadding()

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //отображение id
                    Row(
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            textAlign = TextAlign.Center,
                            text = "${textStENikList.firstOrNull()?.id ?: 0}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    //отображение иконки удаления всех слов с этим id
                    IconButton(
                        onClick = {
                            val allLanguageList = viewModelVocabularyTextEdit.textStENikListForEditScreen.map { it.language }
                            viewModelVocabularyTextEdit.setNewText(allLanguageList, "")
                        }
                    ) {
                        // Используем стандартную иконку Clear (крестик)
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            contentDescription = "Очистить текст"
                        )
                    }
                }

                //отображение словаря
                DropDownVocabularyText(
                    label = "словарь",
                    selectedVocabulary = vocabulary.value,
                    vocabularyStENikList = viewModelVocabularyTextEdit.vocabularyStENikList,
                    returnNewSetVocabulary = { newVocabulary ->
                        viewModelVocabularyTextEdit.setNewVocabulary(newVocabulary)
                    }
                )


                /*
                TextFieldForVocabularyEdit(
                    text = vocabulary.value,
                    label = stringResource(R.string.lbl_vocabulary),
                    onValueChange = { newVocabulary ->
                        viewModelVocabularyEdit.setNewVocabulary(newVocabulary)
                    }
                )

                 */

                //отображение строк с текстом, иконками удаления и микрофона
                LazyColumn(
                    modifier = Modifier.padding(bottom = bottomPadding.value),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Log.d(TAG_STENIK, "отображение")

                    items(items = textStENikList) { textStENik ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextFieldForVocabulary(
                                text = textStENik.textMutableState,
                                label = textStENik.language.descriptionShort,
                                onValueChange = { newText ->
                                    viewModelVocabularyTextEdit.setNewText(listOf(textStENik.language), newText)
                                }
                            )
                            //Иконка микрофона для запуска слушателя
                            //и перевода речи в текст
                            IconButton(
                                modifier = Modifier
                                    .padding(start = 10.dp),
                                onClick = {//Запрос разрешения микрофона и запуск распознавания речи
                                    Log.d(TAG_STENIK, "PERMISSION - getTextBySpeech")
                                    viewModelVocabularyTextEdit.checkPermissionAndStartSpeechRecognizer(
                                        language = textStENik.language,
                                        startLaunch = {//запрос разрешения
                                            launcher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    )
                                },
                                enabled = iconIsEnable.value,
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_speech_to_text),
                                    contentDescription = "",
                                    tint = if (iconIsEnable.value) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }


                        }
                    }
                }
            }
        }
    }
}

