package best.mobile.stenik.ui.screens.vocabulary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import best.mobile.entities.VocabularyStENik
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.vocabulary.custom_view.TextFieldForVocabulary
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenVocabularyNameCustom(
    viewModelVocabularyNameCustom: ViewModelVocabularyNameCustom = koinViewModel(),
    onClickBack: () -> Unit,
) {
    val bottomPadding = remember { mutableStateOf(0.dp) }
    val vocabularyStENikList = remember { viewModelVocabularyNameCustom.vocabularyList }


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
                        viewModelVocabularyNameCustom.saveVocabularyList(vocabularyStENikList.toList())
                        onClickBack()
                    }
                }
            }
        ) { paddingValues ->
            bottomPadding.value = paddingValues.calculateBottomPadding()
            Column(
                modifier = Modifier.padding(bottom = bottomPadding.value),
                verticalArrangement = Arrangement.Top,

                ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(vocabularyStENikList) { index, vocabulary ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //Список словарей
                            TextFieldForVocabulary(
                                label = "",
                                text = vocabulary.name,
                                onValueChange = { newVocabularyName ->
                                    viewModelVocabularyNameCustom.vocabularyList[index] =
                                        VocabularyStENik(
                                            name = newVocabularyName,
                                            isSelected = false
                                        )
                                }
                            )

                            //выбор главного словаря
                            val checkedState =
                                remember { mutableStateOf(viewModelVocabularyNameCustom.vocabularyList[index].isSelected) }//viewModelVocabularyCustom.vocabularyList[index].isSelected//vocabulary.name == viewModelVocabularyCustom.settings.value.vocabularyTextDefault

                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = {
                                    val isChecked = !checkedState.value
                                    checkedState.value = isChecked
                                    viewModelVocabularyNameCustom.vocabularyList[index].isSelected =
                                        isChecked
                                    //viewModelVocabularyCustom.putSettings(vocabulary.name)
                                }
                            )
                        }
                    }
                }

                //Добавить пустой словарь для последующего ввода названия
                Text(
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            viewModelVocabularyNameCustom.vocabularyList.add(
                                VocabularyStENik(
                                    "",
                                    false
                                )
                            )
                        },
                    text = "+  Добавить",
                )
            }

        }
    }
}