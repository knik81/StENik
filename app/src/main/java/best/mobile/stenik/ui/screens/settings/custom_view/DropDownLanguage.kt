package best.mobile.stenik.ui.screens.settings.custom_view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import best.mobile.entities.LanguageStENik

@Composable
fun SelectLanguage(
    label: String,
    second: Boolean = false,
    selectedLanguage: LanguageStENik,
    returnNewSetLanguage: (LanguageStENik) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(modifier = Modifier.weight(3f)) {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
        Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            DropdownMenuLanguage(selectedLanguage, second) {
                returnNewSetLanguage(it)
            }
        }
    }
}