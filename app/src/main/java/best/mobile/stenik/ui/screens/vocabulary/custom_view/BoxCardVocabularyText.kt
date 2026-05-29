package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxCardVocabularyText(text: String) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 5.dp),
            text = text
        )
    }
}