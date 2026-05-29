package best.mobile.stenik.ui.screens.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import best.mobile.entities.SortedTextBy
import best.mobile.entities.Utils.VARIANTS_TEST_MAX
import best.mobile.entities.Utils.VARIANTS_TEST_MIN
import best.mobile.entities.Utils.VARIANTS_TEST_STEPS
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.settings.custom_view.SelectLanguage
import best.mobile.stenik.ui.screens.settings.custom_view.ChooseSortedBy
import best.mobile.stenik.ui.screens.custom_view.HorizontalDividerStENik
import best.mobile.stenik.ui.screens.settings.custom_view.SliderStENik
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenSettings(
    viewModelSettings: ViewModelSettings = koinViewModel(),
    onClickExtra: () -> Unit,
) {
    val scrollStateScreen: ScrollState = rememberScrollState()
    var settingsStENik by remember { viewModelSettings.settingsStENik }
    val newSettingsStENik = settingsStENik.getNewInstance()
    val radioBtnMap = mapOf(
        SortedTextBy.BY_SMART to stringResource(R.string.rdb_smart),
        SortedTextBy.BY_LEVEL to stringResource(R.string.rdb_level),
        SortedTextBy.BY_ID to stringResource(R.string.rdb_id)
    )

    Scaffold(
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Кнопка перехода в доп. настройки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonStENik(stringResource(R.string.btn_extraSettings)) {
                        onClickExtra()
                    }

                }


            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollStateScreen)
            ) {
                // HorizontalDividerStENik()

                //Выбор языка
                Text(
                    text = stringResource(R.string.txt_groupLanguage),
                    style = MaterialTheme.typography.titleLarge
                )

                SelectLanguage(
                    label = stringResource(R.string.txt_mainLanguage),
                    selectedLanguage = viewModelSettings.settingsStENik.value.languageMain
                ) { newSetLanguage ->
                    newSettingsStENik.languageMain = newSetLanguage
                    viewModelSettings.settingsStENik.value = newSettingsStENik
                    viewModelSettings.putSettings()
                }

                SelectLanguage(
                    label = stringResource(R.string.txt_firstLanguage),
                    selectedLanguage = viewModelSettings.settingsStENik.value.languageFirst
                ) { newSetLanguage ->
                    newSettingsStENik.languageFirst = newSetLanguage
                    viewModelSettings.settingsStENik.value = newSettingsStENik
                    viewModelSettings.putSettings()
                }

                SelectLanguage(
                    label = stringResource(R.string.txt_secondLanguage),
                    second = true,
                    selectedLanguage = viewModelSettings.settingsStENik.value.languageSecond
                ) { newSetLanguage ->
                    newSettingsStENik.languageSecond = newSetLanguage
                    viewModelSettings.settingsStENik.value = newSettingsStENik
                    viewModelSettings.putSettings()

                }

                HorizontalDividerStENik()

                //Слушать при запуске
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(3f)) {
                        Text(
                            text = stringResource(R.string.cbx_listenOnStarted),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Column(
                        modifier =
                            Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Checkbox(
                            checked = viewModelSettings.settingsStENik.value.launchListen,
                            onCheckedChange = {
                                newSettingsStENik.launchListen = !newSettingsStENik.launchListen
                                viewModelSettings.settingsStENik.value = newSettingsStENik
                                viewModelSettings.putSettings()
                            }
                        )
                    }
                }


                HorizontalDividerStENik()

                //Метод сортировки текста
                Text(
                    text = stringResource(R.string.txt_sortingBy),
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        radioBtnMap.forEach { radioBtnMapItem ->
                            ChooseSortedBy(
                                radioBtnMapItem = radioBtnMapItem,
                                selectedRadioBtnMapItem = viewModelSettings.settingsStENik.value.sortedTextBy
                            ) { returnNewSetSortedTextBy ->
                                newSettingsStENik.sortedTextBy = returnNewSetSortedTextBy
                                viewModelSettings.settingsStENik.value = newSettingsStENik
                                viewModelSettings.putSettings()
                            }
                        }
                    }
                }

                HorizontalDividerStENik()

                //Вариантов для тестирования
                SliderStENik(
                    setLabel = stringResource(R.string.txt_variantsForTest),
                    setValue = viewModelSettings.settingsStENik.value.amountVariantsForTest.toFloat(),
                    setMin = VARIANTS_TEST_MIN,
                    setMax = VARIANTS_TEST_MAX,
                    setSteps = VARIANTS_TEST_STEPS,
                    isInt = true
                ) { returnNewValue ->
                    newSettingsStENik.amountVariantsForTest = returnNewValue.toInt()
                    viewModelSettings.settingsStENik.value = newSettingsStENik
                    viewModelSettings.putSettings()
                }


                HorizontalDividerStENik()



            }
        }
    }

}
