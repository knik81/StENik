package best.mobile.stenik.ui.screens.listen.custom_view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import best.mobile.entities.TextStENikInterface
import best.mobile.stenik.ui.screens.custom_view.TextForCard

@Composable
fun CardForListen(
    textStENikSpeechList: List<TextStENikInterface>
) {

    //Карточка с текстом для прослушивания
    if (textStENikSpeechList.isNotEmpty())
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 20.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                textStENikSpeechList.forEach { textToSpeechStENikItemItem ->
                    TextForCard(text = textToSpeechStENikItemItem.text)
                }

            }
        }
}