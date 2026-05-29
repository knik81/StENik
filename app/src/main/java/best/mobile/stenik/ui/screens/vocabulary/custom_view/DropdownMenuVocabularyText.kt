package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import best.mobile.entities.VocabularyStENik

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuVocabularyText(
    selectedVocabulary: VocabularyStENik?,
    vocabularyStENikList: List<VocabularyStENik>,
    returnNewSetVocabulary: (VocabularyStENik) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    //val items = listOf<VocabularyStENik>()
    val focusManager = LocalFocusManager.current


    //Log.d(TAG_STENIK, "selectedText 1 = ${selectedLanguage.descriptionShort}")

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp, vertical = 2.dp)
                .fillMaxWidth()//.sizeIn(minWidth = 100.dp)
                .clickable { expanded.value = true }
        ) {

            Text(text = selectedVocabulary?.name.toString())

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                vocabularyStENikList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                selectionOption.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        onClick = {
                            returnNewSetVocabulary(selectionOption)
                            expanded.value = false
                           focusManager.clearFocus() // Скрываем клавиатуру, если она была активна
                        },
                    )
                }
            }
        }
    }
}