package best.mobile.stenik.ui.screens.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import best.mobile.stenik.R

@Composable
fun PopupStENik(
    openPopup: MutableState<Boolean>,
    setLabel: String,
    onClickYes: () -> Unit
) {
    if (openPopup.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(focusable = true, dismissOnClickOutside = true)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // Полупрозрачный серый цвет для затемнения фона
                        .background(Color.Gray.copy(alpha = 0.6f))
                        // 3. Добавляем клик, чтобы закрыть Popup при нажатии на затемненную область
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            openPopup.value = false
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        // color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)

                        ) {
                            Text(setLabel)
                            //Text(stringResource(R.string.txt_replaceFromCloud))

                            Spacer(Modifier.height(8.dp))
                            ButtonStENik(stringResource(R.string.btn_replaceFromCloud)) {
                                onClickYes()
                            }
                            ButtonStENik(stringResource(R.string.btn_close)) {
                                openPopup.value = false
                            }
                        }
                    }
                }
            }
        }
    }

}