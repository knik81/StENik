package best.mobile.stenik.ui.screens.test.custom_view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import best.mobile.entities.TextStENikInterface
import best.mobile.stenik.ui.screens.custom_view.TextForCard

@Composable
fun CardForTest(
    textStENikList: List<TextStENikInterface>,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onClick: (List<TextStENikInterface>) -> Unit
) {

    //Карточка с текстом
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable {
                onClick(textStENikList)
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // verticalArrangement = Arrangement.Center
        ) {

            textStENikList.forEach { textStENik ->
                Row(
                    modifier = Modifier
                    //.fillMaxSize()
                ) {
                    TextForCard(
                        text = textStENik.text
                    )
                }
            }
        }
    }

}
