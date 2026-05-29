package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import best.mobile.entities.TextStENik

@Composable
fun CardForVocabularyText(
    textStENikList: List<TextStENik>,
    onClick: (List<TextStENik>) -> Unit
) {


    //Карточка с текстом для словаря
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable {
                onClick(textStENikList)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BoxCardVocabularyText(textStENikList.firstOrNull()?.level.toString())
                BoxCardVocabularyText(textStENikList.firstOrNull()?.vocabulary.toString())
                BoxCardVocabularyText(textStENikList.firstOrNull()?.id.toString())
            }

            textStENikList.forEach { textStENik ->
                Row(modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = textStENik.language.descriptionShort
                    )
                    Text(text = textStENik.text)
                }
            }
        }
    }
}
