package best.mobile.stenik.ui.screens.settings.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderStENik(
    setLabel: String,
    labelIsTxt: Boolean = false,
    setValue: Float,
    setMin: Float,
    setMax: Float,
    isInt: Boolean,
    setSteps: Int = (setMax - setMin - 1).toInt(),
    getNewValue: (Float) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            //Определяется текст заголовка или текст позиции. Разница только в стиле и расположении.
            if (!labelIsTxt)
                Text(
                    text = setLabel,
                    style = MaterialTheme.typography.titleLarge
                )
            else Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
                ) {
                Text(
                    text = setLabel
                )
            }
        }
        //Установка значения.
        //выбор для округления
        Row(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isInt)
                Text(
                    text = setValue.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            else
                Text(
                    text = setValue.toInt().toString(),
                    style = MaterialTheme.typography.titleLarge
                )
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = setValue, // Текущее значение
            onValueChange = { newValue ->
                getNewValue(newValue)
            },
            valueRange = setMin..setMax, // Диапазон значений
            steps = setSteps,// Количество шагов между началом и концом (0 - непрерывный)
            track = {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            },
        )

        //Подписи под слайдером с минимальным и максимальным значением
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (!isInt) {
                Text(
                    text = setMin.toString(),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = setMax.toString(),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            } else {
                Text(
                    text = setMin.toInt().toString(),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = setMax.toInt().toString(),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
        }
    }
}