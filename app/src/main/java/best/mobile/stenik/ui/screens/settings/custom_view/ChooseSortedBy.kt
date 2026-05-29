package best.mobile.stenik.ui.screens.settings.custom_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import best.mobile.entities.SortedTextBy

@Composable
fun ChooseSortedBy(
    radioBtnMapItem: Map.Entry<SortedTextBy, String>,
    selectedRadioBtnMapItem: SortedTextBy,
    returnNewSetSortedTextBy: (SortedTextBy) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(modifier = Modifier.weight(3f)) {
            Text(
                text = radioBtnMapItem.value,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Row (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {

            RadioButton(modifier = Modifier.height(8.dp),
                selected = (radioBtnMapItem.key == selectedRadioBtnMapItem),
                onClick = { returnNewSetSortedTextBy(radioBtnMapItem.key) }
            )


        }
    }
}