package best.mobile.data.tts

import android.content.Context
import android.content.Intent
import android.util.Log
import best.mobile.data.repositoty.RepositoryMain
import best.mobile.data.tts.service.ServiceTTS
import best.mobile.entities.LanguageStENik
import best.mobile.entities.SpeechRecognitionResultStENik
import best.mobile.entities.SpeechRecognizerStENikInterface
import best.mobile.entities.TextStENikSpeech
import best.mobile.entities.Utils.TAG_STENIK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class MainTTS(
    private val repositoryMain: RepositoryMain,
    private val TTStENik: TTStENik,
    private val speechRecognizerStENik: SpeechRecognizerStENikInterface
) {


    fun startServiceTTS(context: Context) {
        val intent = Intent(context, ServiceTTS::class.java)
        context.startService(intent)
    }



    fun stopTTS(context: Context) {
        val intent = Intent(context, ServiceTTS::class.java)
        context.stopService(intent)
        TTStENik.cancelTTS()
    }


    suspend fun startTTS(
        context: Context,
        cardTextListChange: (MutableList<TextStENikSpeech>) -> Unit
    ) {
        TTStENik.startTTS(
            context = context,
            repositoryMain = repositoryMain,
            cardTextListChange = { cardTextList -> cardTextListChange(cardTextList) },
            setStopTTS = { stopTTS(context) },
        )
    }

    fun pauseTTS() {
        TTStENik.pauseTTS()
    }

    //получить текст из речи с микрофона
    private val _recognitionState = MutableStateFlow<SpeechRecognitionResultStENik?>(null)
    val recognitionState: StateFlow<SpeechRecognitionResultStENik?> =
        _recognitionState.asStateFlow()

    @OptIn(DelicateCoroutinesApi::class)
    fun getTextBySpeech(language: LanguageStENik) {
        Log.d(TAG_STENIK, "getTextBySpeech")

        GlobalScope.launch(Dispatchers.Main) {
            //Log.d(TAG_STENIK, "GlobalScope")
            speechRecognizerStENik.startListening(setLanguage = language)
                .catch { e ->
                    // Обработка исключений, которые могут закрыть Flow (например, Permission Error)
                    _recognitionState.value =
                        SpeechRecognitionResultStENik.Error(e.message ?: "Неизвестная ошибка")
                    Log.d(TAG_STENIK, "ошибка = $e")
                }
                .collect { result ->
                    // Получение всех состояний: Ready, Speaking, Success и т.д.
                    _recognitionState.value = result
                    Log.d(TAG_STENIK, "result = $result")
                    //Задержка для смены состояния
                    delay(1500L)
                    //Обязательно сменить состояние.
                    //Иначе текст останется в переменной и при переключении экрана он будет тянутся
                    _recognitionState.value = SpeechRecognitionResultStENik.IsFinish
                }
        }
    }

    fun stopTextBySpeech() {
        speechRecognizerStENik.stopListening()
    }
}

