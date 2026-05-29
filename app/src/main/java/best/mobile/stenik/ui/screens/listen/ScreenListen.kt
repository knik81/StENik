package best.mobile.stenik.ui.screens.listen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.PopupStENik
import best.mobile.stenik.ui.screens.listen.custom_view.CardForListen
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@SuppressLint(
    "UnrememberedMutableState", "ConfigurationScreenWidthHeight",
    "CoroutineCreationDuringComposition"
)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable

fun ScreenListen(
    viewModelListen: ViewModelListen = koinViewModel()
) {

    val newSettingsStENik = remember { viewModelListen.settingsStENik.value.getNewInstance() }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val window = remember { (context as? Activity)?.window }

    val activity = remember { context as? Activity }
    val notificationManager = remember {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    //вычисление высоты для LazyColum
    val configuration = LocalConfiguration.current
    var bottomHeight by remember { mutableStateOf(0.dp) }
    var screenHeightDpLazyColum by remember { mutableStateOf(0.dp) }
    screenHeightDpLazyColum = configuration.screenHeightDp.dp - bottomHeight * 0.85f

    val scrollStateScreen: ScrollState = rememberScrollState()

    val cardTextList =
        viewModelListen.cardTextList.last()

    val openPopup = remember { mutableStateOf(false) }

    //Лаунчер для запроса разрешения на уведомления
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    Scaffold(
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Чекбокс переключения фоновой работы
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (viewModelListen.showButtonStartTTS.value && !viewModelListen.isPause.value) {
                                viewModelListen.pressCheckedPlayBackground.value = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        enabled = viewModelListen.showButtonStartTTS.value && !viewModelListen.isPause.value,
                        checked = viewModelListen.isCheckedPlayBackground.value,
                        onCheckedChange = {
                            viewModelListen.pressCheckedPlayBackground.value = true
                        }
                    )
                    Text(
                        text = stringResource(R.string.cbx_backgroundListen),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                //Кнопки
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (viewModelListen.showButtonStartTTS.value) {
                        //Кнопка слушать
                        Button(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            onClick = {
                                viewModelListen.pressButtonStart.value = true
                                //!viewModelListen.pressButtonStart.value
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                                contentDescription = ""
                            )
                        }
                    }
                    if (viewModelListen.showButtonStop.value) {
                        //Кнопка стоп
                        Button(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            onClick = {
                                viewModelListen.pressButtonStop.value = true
                                //!viewModelListen.pressButtonStop.value
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.ic_stop),
                                contentDescription = ""
                            )
                        }
                    }

                    //Кнопка пауза
                    Button(
                        enabled = viewModelListen.enabledButtonPause.value,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        onClick = {
                            viewModelListen.pressButtonPause.value = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_pause),
                            contentDescription = ""
                        )
                    }

                }
            }
        }
    ) { paddingValues ->
        //вывод текста на экран
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollStateScreen)
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(screenHeightDpLazyColum)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(cardTextList) { textToSpeechStENikItem ->
                    CardForListen(listOf(textToSpeechStENikItem))
                }
            }
        }
    }

    // Нажатие кнопки PlayTTS.
    if (viewModelListen.pressButtonStart.value) {
        val check = viewModelListen.startTTS(
            context = context,
            moveTaskToBack = {
                activity?.window?.decorView?.let {
                    it.post { activity.moveTaskToBack(true) }
                }
            },
            startLaunch = {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            })
        //check показывает что запущен TTS
        if (check) {
            viewModelListen.showButtonStartTTS.value = false
            viewModelListen.enabledButtonPause.value = true
            viewModelListen.showButtonStop.value = true
            viewModelListen.pressButtonStart.value = false
            viewModelListen.isPause.value = false
        }
    }

    // Нажатие кнопки Stop.
    if (viewModelListen.pressButtonStop.value) {
        viewModelListen.showButtonStartTTS.value = true
        viewModelListen.enabledButtonPause.value = false
        viewModelListen.showButtonStop.value = false
        viewModelListen.pressButtonStop.value = false
        viewModelListen.isPause.value = false

        viewModelListen.stopTTS()
        notificationManager.cancelAll()
    }

    // Нажатие кнопки Pause.
    if (viewModelListen.pressButtonPause.value) {
        viewModelListen.showButtonStartTTS.value = true
        viewModelListen.enabledButtonPause.value = false
        viewModelListen.showButtonStop.value = false
        viewModelListen.pressButtonPause.value = false
        viewModelListen.isPause.value = true
        viewModelListen.pauseTTS()
    }

    // Нажатие чекбокса фонового запуска.
    if (viewModelListen.pressCheckedPlayBackground.value) {
        viewModelListen.isCheckedPlayBackground.value =
            !viewModelListen.isCheckedPlayBackground.value//newValue
        newSettingsStENik.playBackground = !newSettingsStENik.playBackground//newValue//обновить
        viewModelListen.putSettings(newSettingsStENik)//сохранить настройки
        viewModelListen.pressCheckedPlayBackground.value = false
    }



    LaunchedEffect(1) {
        //Нажатие кнопки прослушивания
        viewModelListen.pressButtonStart.value = viewModelListen.launchListen

        //слушатель для экрана батареи
        viewModelListen.uiEvent.collect { event ->
            when (event) {
                //экран для настроек батареи
                ViewModelListen.UiEvent.OpenBatterySettings -> {
                    openPopup.value = true
                }
            }
        }
    }


    //Изменить настройки батареи
    if (openPopup.value) {
        PopupStENik(
            setLabel = stringResource(R.string.txt_batterySet1) + "\n" + stringResource(R.string.txt_batterySet2),
            openPopup = openPopup,
            onClickYes = {
                openPopup.value = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                }

                // Проверка на случай, если Intent не может быть обработан
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        )
    }

    LaunchedEffect(1) {
        delay(10000)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    //Жизненный цикл экрана
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY)
                viewModelListen.stopTTS() //останавливать джоб ТТS

        }
        // Добавляем наблюдателя к жизненному циклу
        lifecycleOwner.lifecycle.addObserver(observer)

        // Удаление наблюдателя при удалении Composable из композиции
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            viewModelListen.stopTTS() //останавливать джоб ТТS
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


}






