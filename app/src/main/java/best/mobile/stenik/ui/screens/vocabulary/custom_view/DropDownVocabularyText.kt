package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import best.mobile.entities.VocabularyStENik

@Composable
fun DropDownVocabularyText(
    label: String,
    selectedVocabulary: VocabularyStENik?,
    vocabularyStENikList: List<VocabularyStENik>,
    returnNewSetVocabulary: (VocabularyStENik) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = label,
            //modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        DropdownMenuVocabularyText(
            selectedVocabulary = selectedVocabulary,
            vocabularyStENikList = vocabularyStENikList
        ) {
            returnNewSetVocabulary(it)
        }
        /*
        Box(
            modifier = Modifier.weight(3f)
        ) {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            DropdownMenuVocabulary(
                selectedVocabulary = selectedVocabulary,
                vocabularyStENikList = vocabularyStENikList
            ) {
                returnNewSetVocabulary(it)
            }
        }

         */
    }
}