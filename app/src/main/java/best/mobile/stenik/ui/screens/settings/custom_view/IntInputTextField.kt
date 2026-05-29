package best.mobile.stenik.ui.screens.settings.custom_view

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IntInputTextField(
    label: String,
    value: Int,
    returnValue: (Int) -> Unit // Callback для возврата значения Int
) {

    OutlinedTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            // Фильтрация ввода: разрешены только цифры
            val filteredValue = newValue.filter { it.isDigit()}// || it == '.' }

            // Попытка конвертации в Int и вызов callback
            val intValue = filteredValue.toIntOrNull()
            if (intValue != null) {
                returnValue(intValue)
            } else if (filteredValue.isEmpty()) {
                // Обработка случая, когда поле пустое
                returnValue(34)
            }
        },
        label = { Text(label) },
        //Настройка клавиатуры на числовую
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}