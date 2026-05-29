package best.mobile.stenik.ui.screens.custom_view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextForCard(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}