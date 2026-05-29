package best.mobile.stenik.ui.screens.custom_view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDividerStENik() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        thickness = 1.dp
    )
}