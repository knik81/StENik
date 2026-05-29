package best.mobile.stenik.ui.screens.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import best.mobile.entities.Utils.DELAY_MAX
import best.mobile.entities.Utils.DELAY_MIN
import best.mobile.entities.Utils.DELAY_STEPS
import best.mobile.entities.Utils.MIC_STEPS
import best.mobile.entities.Utils.MIC_TIME_MAX
import best.mobile.entities.Utils.MIC_TIME_MIN
import best.mobile.entities.Utils.REPEAT_MAX
import best.mobile.entities.Utils.REPEAT_MIN
import best.mobile.entities.Utils.REPEAT_STEPS
import best.mobile.entities.Utils.SPEED_MAX
import best.mobile.entities.Utils.SPEED_MIN
import best.mobile.entities.Utils.SPEED_STEPS
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.HorizontalDividerStENik
import best.mobile.stenik.ui.screens.settings.custom_view.SliderStENik
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun ScreenSettingsExtra(
    viewModelSettings: ViewModelSettings = koinViewModel(),
) {
    val scrollStateScreen: ScrollState = rememberScrollState()
    var settingsStENik by remember { viewModelSettings.settingsStENik }
    val newSettingsStENik = settingsStENik.getNewInstance()

    Column{
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollStateScreen)
        ) {

            /*
            //Текст брать только из основного словаря
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(3f)) {
                    Text(
                        text = stringResource(R.string.cbx_textOnlyMainVocabulary),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(
                    modifier =
                        Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Checkbox(
                        checked = viewModelSettings.settingsStENik.value.textOnlyDefaultVocabulary,
                        onCheckedChange = {
                            newSettingsStENik.textOnlyDefaultVocabulary =
                                !newSettingsStENik.textOnlyDefaultVocabulary
                            viewModelSettings.settingsStENik.value = newSettingsStENik
                            viewModelSettings.putSettings()
                        }
                    )
                }
            }

            HorizontalDividerStENik()
            */

            //Количество повторений
            SliderStENik(
                setLabel = stringResource(R.string.txt_repeatListenAmount),
                setValue = viewModelSettings.settingsStENik.value.repeatListenAmount.toFloat(),
                setMin = REPEAT_MIN,
                setMax = REPEAT_MAX,
                setSteps = REPEAT_STEPS,
                isInt = true
            ) { returnNewValue ->
                newSettingsStENik.repeatListenAmount = returnNewValue.toInt()
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }

            //Повторять все
            //отключил
            /*
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.cbx_repeatEverything),
                        //style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(
                    modifier =
                        Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Checkbox(
                        checked = viewModelSettings.settingsStENik.value.repeatEverything,
                        onCheckedChange = {
                            newSettingsStENik.repeatEverything = !newSettingsStENik.repeatEverything
                            viewModelSettings.settingsStENik.value = newSettingsStENik
                            viewModelSettings.putSettings()
                        }
                    )
                }
            }
            */

            HorizontalDividerStENik()

            //Время прослушивания речи при редактировании текста и переводе его в текст
            //Количество повторений
            SliderStENik(
                setLabel = stringResource(R.string.txt_timeMillsSecForSpeech),
                setValue = viewModelSettings.settingsStENik.value.timeMillsSecForSpeech.toFloat(),
                setMin = MIC_TIME_MIN,
                setMax = MIC_TIME_MAX,
                isInt = true,
                setSteps = MIC_STEPS
            ) { returnNewValue ->
                newSettingsStENik.timeMillsSecForSpeech = returnNewValue.toInt()
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }


            HorizontalDividerStENik()


            //Облако
            /*
           Text(
               text = stringResource(R.string.txt_cloud),
               style = MaterialTheme.typography.titleLarge
           )

           Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically
           ) {
               Row(
                   modifier = Modifier
                       .weight(3f)
                       .fillMaxWidth(),
                   horizontalArrangement = Arrangement.End
               ) {
                   Text(
                       text = stringResource(R.string.cbx_saveToCloud),
                   )
               }
               Column(
                   modifier = Modifier.weight(1f),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Checkbox(
                       checked = viewModelSettings.settingsStENik.value.autoSaveToFirebase,
                       onCheckedChange = {
                           newSettingsStENik.autoSaveToFirebase =
                               !newSettingsStENik.autoSaveToFirebase
                           viewModelSettings.settingsStENik.value = newSettingsStENik
                           viewModelSettings.putSettings()
                       }
                   )
               }
           }
           Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically
           ) {
               Row(
                   modifier = Modifier
                       .weight(3f)
                       .fillMaxWidth(),
                   horizontalArrangement = Arrangement.End
               ) {
                   Text(
                       text = stringResource(R.string.cbx_loadFromCloud),
                   )
               }
               Column(
                   modifier =
                       Modifier
                           .weight(1f)
                           .height(20.dp),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Checkbox(
                       checked = viewModelSettings.settingsStENik.value.autoLoadFromFirebase,
                       onCheckedChange = {
                           newSettingsStENik.autoLoadFromFirebase =
                               !newSettingsStENik.autoLoadFromFirebase
                           viewModelSettings.settingsStENik.value = newSettingsStENik
                           viewModelSettings.putSettings()
                       }
                   )
               }
           }


           HorizontalDividerStENik()

             */

            //Скорость чтения

            Text(
                text = stringResource(R.string.txt_readingSpeed),
                style = MaterialTheme.typography.titleLarge
            )

            SliderStENik(
                setLabel = stringResource(R.string.txt_mainLanguage),
                labelIsTxt = true,
                setValue = viewModelSettings.settingsStENik.value.readSpeedMainLanguage,
                setMin = SPEED_MIN,
                setMax = SPEED_MAX,
                isInt = false,
                setSteps = SPEED_STEPS
            ) { returnNewValue ->
                newSettingsStENik.readSpeedMainLanguage = round(returnNewValue * 10) / 10f
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }

            SliderStENik(
                setLabel = stringResource(R.string.txt_firstLanguage),
                labelIsTxt = true,
                setValue = viewModelSettings.settingsStENik.value.readSpeedFirstLanguage,
                setMin = SPEED_MIN,
                setMax = SPEED_MAX,
                isInt = false,
                setSteps = SPEED_STEPS
            ) { returnNewValue ->
                newSettingsStENik.readSpeedFirstLanguage = round(returnNewValue * 10) / 10f
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }

            SliderStENik(
                setLabel = stringResource(R.string.txt_secondLanguage),
                labelIsTxt = true,
                setValue = viewModelSettings.settingsStENik.value.readSpeedSecondLanguage,
                setMin = SPEED_MIN,
                setMax = SPEED_MAX,
                isInt = false,
                setSteps = SPEED_STEPS
            ) { returnNewValue ->
                newSettingsStENik.readSpeedSecondLanguage = round(returnNewValue * 10) / 10f
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }

/*
            HorizontalDividerStENik()

            //Формула
            Text(
                text = stringResource(R.string.txt_formula),
                style = MaterialTheme.typography.titleLarge
            )

            IntInputTextField(
                label = "SIGMA",
                value = viewModelSettings.settingsStENik.value.sigma
            ) { returnNewValue ->
                newSettingsStENik.sigma = returnNewValue
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()

            }

 */


            /*
           FloatInputTextField(
               label = "B",
               value = viewModelSettings.settingsStENik.value.bCof
           ) {returnNewValue ->
               newSettingsStENik.bCof = returnNewValue
               viewModelSettings.settingsStENik.value = newSettingsStENik
               viewModelSettings.putSettings()

           }
   */


            HorizontalDividerStENik()

            //Задержка перед чтением
            SliderStENik(
                setLabel = stringResource(R.string.txt_delayReadText),
                setValue = viewModelSettings.settingsStENik.value.delayReadText.toFloat() / 1000,
                setMin = DELAY_MIN / 1000,
                setMax = DELAY_MAX / 1000,
                setSteps = DELAY_STEPS,
                isInt = false
            ) { returnNewValue ->
                newSettingsStENik.delayReadText = (returnNewValue * 1000).toInt()
                viewModelSettings.settingsStENik.value = newSettingsStENik
                viewModelSettings.putSettings()
            }
        }

   }


}