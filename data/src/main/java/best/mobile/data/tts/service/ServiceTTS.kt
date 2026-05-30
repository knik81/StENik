package best.mobile.data.tts.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.annotation.RequiresApi
import best.mobile.data.repositoty.RepositoryMain
import best.mobile.data.tts.TTStENik
import best.mobile.entities.LanguageStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.LANGUAGE_LIST
import best.mobile.entities.Utils.PAUSE_PLAY_TTS
import best.mobile.entities.Utils.TEXT_STENIK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class ServiceTTS : Service() {

    private val repositoryMain: RepositoryMain by inject()
    private val TTStENikClass: TTStENik by inject()
    private val serviceContext: Context = this

    //Переключил поток у корутины. Может и не нужно было...
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var jobTTS: Job? = null //Создание джоба для того, чтобы можно было его остановить


    //список всех слов без сортировки, как из БД загрузили
    private val textStENikList = mutableListOf<TextStENik>()

    //Список языков
    private val languageList = mutableListOf<LanguageStENik>()

    override fun onCreate() {
        super.onCreate()
        //для управления проигрыванием из верхнего уведомления
        TtsStateManager.setTTStENik(setTTStENik = TTStENikClass)
        //Log.d(TAG_STENIK, "ServiceTTS onCreate")
    }

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Log.d(TAG_STENIK, "ServiceTTS onStartCommand")

        when (intent?.action) {
            PAUSE_PLAY_TTS -> {
                // Переключаем или устанавливаем состояние паузы
                TtsStateManager.pressButtonPaused()
            }
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        val textStENikListTemp =
            intent?.getParcelableArrayListExtra(
                TEXT_STENIK,
                TextStENik::class.java
            )?.toMutableList()
        if (textStENikListTemp != null)
            textStENikList.addAll(textStENikListTemp) //чтобы не мучится с nullable типом, переложил с проверкой на null

        //загрузка languageList из intent
        val languageListTemp =
            intent?.getParcelableArrayListExtra(//получить все слова из ViewModel
                LANGUAGE_LIST,
                LanguageStENik::class.java
            )?.toMutableList()
        if (languageListTemp != null)
            languageList.addAll(languageListTemp)//чтобы не мучится с nullable типом, переложил с проверкой на null

        //Запуск джоба. В нем будет запущен TTS
        if (!(jobTTS?.isActive ?: false))
            jobTTS = serviceScope.launch {

                TTStENikClass.startTTS(
                    context = serviceContext,
                    repositoryMain = repositoryMain,
                    powerManager = powerManager,
                    cardTextListChange = {},
                    setStopTTS = { TTStENikClass.cancelTTS() }
                )
            }

        // Возвращаем START_STICKY, чтобы служба перезапускалась, если она будет убита системой
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        jobTTS?.cancel()
        //Log.d(TAG_STENIK, "Служба остановлена")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Для ненавязчивых служб (не привязанных)
    }


}