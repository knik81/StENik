package best.mobile.stenik.ui.screens.listen

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.pm.PackageManager
import android.os.PowerManager
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import best.mobile.data.tts.TTStENik
import best.mobile.data.tts.service.ServiceTTS
import best.mobile.data.tts.service.TtsStateManager
import best.mobile.domain.UseCaseRepository
import best.mobile.domain.UseCaseSpeechTTS
import best.mobile.entities.SettingsStENik
import best.mobile.entities.TextStENikSpeech
import best.mobile.entities.Utils.NAME_1
import best.mobile.entities.Utils.NOTIFICATION_CHANNEL_ID
import best.mobile.entities.Utils.NOTIFICATION_ID
import best.mobile.entities.Utils.PAUSE_PLAY_TTS
import best.mobile.stenik.MainActivity
import best.mobile.stenik.R
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ViewModelListen(
    private val useCaseSpeechTTS: UseCaseSpeechTTS,
    private val useCaseRepository: UseCaseRepository,
    application: Application
) : AndroidViewModel(application) {

    //экземпляр TTStENik из сервиса
    private val tTStENik: MutableStateFlow<TTStENik?> = MutableStateFlow(null)

    //Состояние кнопок и переменные для их нажатия
    val pressButtonStop: MutableState<Boolean> = mutableStateOf(false)
    val pressButtonPause: MutableState<Boolean> = mutableStateOf(false)
    val pressButtonStart: MutableState<Boolean> = mutableStateOf(true)//true для первичной обработки в блоке init
    val pressCheckedPlayBackground: MutableState<Boolean> = mutableStateOf(false)
    val showButtonStartTTS: MutableState<Boolean> = mutableStateOf(true)
    val showButtonStop: MutableState<Boolean> = mutableStateOf(false)
    val enabledButtonPause: MutableState<Boolean> = mutableStateOf(false)

    val isPause: MutableState<Boolean> = mutableStateOf(false)

    init {
        //Подписка на нажатие кнопки из шторки с уведомлением
        viewModelScope.launch {
            TtsStateManager.isPaused.collect { isPaused ->
                pressButtonPause.value = isPaused
                if (!isPaused)
                    pressButtonStart.value = !pressButtonStart.value
            }
        }
        //Подписка на экземпляр TTStENik из сервиса
        viewModelScope.launch {
            TtsStateManager.tTStENik.collect { setTTStENik ->
                tTStENik.value = setTTStENik
            }
        }
    }


    private val _settingsStENik = mutableStateOf(useCaseRepository.getSettings())
    val settingsStENik: MutableState<SettingsStENik>
        get() = _settingsStENik
    val launchListen =
        settingsStENik.value.launchListen

    private val appContext = application.applicationContext
    private val scope = viewModelScope

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    var isCheckedPlayBackground = mutableStateOf(settingsStENik.value.playBackground)

    private val _cardTextList = mutableStateListOf(mutableListOf<TextStENikSpeech>())
    val cardTextList: SnapshotStateList<MutableList<TextStENikSpeech>>
        get() = _cardTextList

    // Канал для отправки навигационных событий
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    sealed class UiEvent {
        object OpenBatterySettings : UiEvent()
    }

    //Разрешение батареи работать без ограничений
    private fun onBatterySettingsClick() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.OpenBatterySettings)
        }
    }


    //Остановить чтения текста и сервис
    fun stopTTS() {
        useCaseSpeechTTS.stopTTS(appContext)
    }

    //Запустить ТTS
    fun startTTS(
        context: Context,
        moveTaskToBack: () -> Unit,
        startLaunch: () -> Unit
    ): Boolean {
        //Проверка, было ли поставлено на паузу
        if (!isPause.value)
            //Ветка с проверкой и запуском TTS
            if (isCheckedPlayBackground.value) {
                if (!checkPermissionAndStartServiceTTS()) {
                    startLaunch()
                    return false
                } else {
                    //запустить TTS в фоне
                    useCaseSpeechTTS.startServiceTTS(appContext)
                    notificationTTS(notificationManager, appContext)
                    //stopTTS()
                    moveTaskToBack()
                    return true
                }

            } else {
                //запустить онлайн TTS
                scope.launch {
                    useCaseSpeechTTS.startTTS(
                        context = context,
                        cardTextListChange = { textToSpeechStENikItem ->
                            _cardTextList.add(textToSpeechStENikItem)
                        })
                }
                return true
            }
        else {
            //Снятие с паузы
            pauseTTS()
            return true
        }
    }

    //Сохранить настройки
    fun putSettings(newSettings: SettingsStENik) {
        useCaseRepository.putSettings(newSettings)
    }

    //Постановка/снятие с паузы
    fun pauseTTS() {
        if (!isCheckedPlayBackground.value)
            //Пауза ТTS онлайн
            useCaseSpeechTTS.pauseTTS()
        else {
            //Пауза ТTS в фоне
            tTStENik.value?.pauseTTS()
        }
    }

    //проверка разрешения и запуск ТТS
    private fun checkPermissionAndStartServiceTTS(): Boolean {
        when (PackageManager.PERMISSION_GRANTED) {
            //проверка уведомления
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                //проверка режима питания для закрытия фоновых задач
                if (isIgnoringBatteryOptimizations(appContext)) {
                    //запустить в фоне ТТS
                    return true
                } else {
                    //открыть настройки, чтобы отключить фоновые ограничения батареи
                    onBatterySettingsClick()
                    return false
                }
            }

            else -> {
                Toast.makeText(
                    appContext,
                    appContext.getString(R.string.msg_grantNotification),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
    }

    private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName = context.packageName
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }


}


//Уведомление вверху экрана
private fun notificationTTS(notificationManager: NotificationManager, context: Context) {
    //Создание канала
    val channel =
        NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "ServiceTTS",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "ServiceTTS Listener"
        }
    notificationManager.createNotificationChannel(channel)

    //pendingIntent от MainActivity для открытия свернутого приложения при нажатии на иконку уведомления
    val intentMainActivity = Intent(context, MainActivity::class.java)
    intentMainActivity.flags = FLAG_ACTIVITY_REORDER_TO_FRONT or FLAG_ACTIVITY_SINGLE_TOP
    val pendingIntentMainActivity: PendingIntent =
        PendingIntent.getActivity(context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE)

    // Создание PendingIntent для кнопки "Пауза/Играть"
    val pauseIntent =
        Intent(context, ServiceTTS::class.java)
    pauseIntent.action = PAUSE_PLAY_TTS
    pauseIntent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT or FLAG_ACTIVITY_SINGLE_TOP
    val pendingPauseIntent =
        PendingIntent.getService(context, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

    //Создание уведомления
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setContentTitle(context.getString(R.string.lbl_notification))
        .setContentText(NAME_1)
        .setContentIntent(pendingIntentMainActivity)
        .addAction(
            R.drawable.ic_pause,
            context.getString(R.string.btb_pausePlay),
            pendingPauseIntent
        )
        .setSmallIcon(R.drawable.ic_play)
        .build()

    //Отображение уведомления
    notificationManager.notify(NOTIFICATION_ID, notification)
}

