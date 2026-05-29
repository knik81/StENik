package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun SearchTextStENik(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        //отображение словаря
        TextFieldForVocabulary(
            text = text,
            label = label,
            onValueChange = { newText ->
                onValueChange(newText)
                onSearch(newText)
            }
        )

/*
        IconButton(
            onClick = {
                onSearch(text)
            },
            enabled = true
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_find),
                contentDescription = "Поиск"
            )
        }
  */

    }
}