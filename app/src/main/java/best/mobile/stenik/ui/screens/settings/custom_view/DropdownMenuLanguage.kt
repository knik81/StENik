package best.mobile.stenik.ui.screens.settings.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import best.mobile.entities.LanguageStENik

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuLanguage(
    selectedLanguage: LanguageStENik,
    second: Boolean,
    returnNewSetLanguage: (LanguageStENik) -> Unit

) {
    val expanded = remember { mutableStateOf(false) }
    val items = LanguageStENik.entries.filter { it != LanguageStENik.EMPTY }
    val itemsSecond = LanguageStENik.entries // Возможность пустоты
    val focusManager = LocalFocusManager.current

    //Log.d(TAG_STENIK, "selectedText 1 = ${selectedLanguage.descriptionShort}")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp, vertical = 2.dp)
                .sizeIn(minWidth = 25.dp)
                .clickable {
                    expanded.value = true
                }
        ) {
            Text(text = selectedLanguage.descriptionShort)

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                if (!second)
                    items.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    selectionOption.descriptionShort,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            },
                            onClick = {
                                returnNewSetLanguage(selectionOption)
                                expanded.value = false
                                focusManager.clearFocus() // Скрываем клавиатуру, если она была активна
                            },
                        )
                    }
                else//для второго языка можно выбрать пустоту
                    itemsSecond.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    selectionOption.descriptionShort,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            },
                            onClick = {
                                returnNewSetLanguage(selectionOption)
                                expanded.value = false
                                focusManager.clearFocus() // Скрываем клавиатуру, если она была активна
                            },
                        )
                    }
            }
        }
    }
}