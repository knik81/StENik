package best.mobile.stenik.ui.screens.vocabulary.custom_view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import best.mobile.stenik.R

@Composable
fun TextFieldForVocabulary(
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 10.dp),
        value = text,
        label = {
            Text(text = label)
                },
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(20.dp),
        maxLines = 3,//singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),

        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    // Используем стандартную иконку Clear (крестик)
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = "Очистить текст"
                    )
                }


            }
        }
    )
}