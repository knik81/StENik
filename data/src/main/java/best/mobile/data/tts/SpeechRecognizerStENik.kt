package best.mobile.data.tts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import best.mobile.entities.LanguageStENik
import best.mobile.entities.SpeechRecognitionResultStENik
import best.mobile.entities.SpeechRecognizerStENikInterface
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class SpeechRecognizerStENik(
    private val context: Context
) : SpeechRecognizerStENikInterface {
    private var speechRecognizer: SpeechRecognizer? = null
    private var language = LanguageStENik.EMPTY

    override fun startListening(setLanguage: LanguageStENik): Flow<SpeechRecognitionResultStENik> =
        callbackFlow {

            language = setLanguage
            //Log.d(TAG_STENIK, "language = $language")

            // 1. Создание RecognitionListener с доступом к ProducerScope
            val listener = createRecognitionListener(this)

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(listener)
            }

            // 2. Запуск прослушивания
            val recognizerIntent = createRecognizerIntent(language.locale.toLanguageTag())
            //Log.d(TAG_STENIK, "language.locale.language) = ${language.locale}")
            speechRecognizer?.startListening(recognizerIntent)

            // 3. awaitClose вызывается при отмене Flow или при вызове close()
            awaitClose {
                speechRecognizer?.destroy()
                speechRecognizer = null
            }
        }

    override fun stopListening() {
        speechRecognizer?.stopListening()
    }

    // --- Вспомогательные функции ---

    // Функция, преобразующая события RecognitionListener в Flow
    private fun createRecognitionListener(scope: ProducerScope<SpeechRecognitionResultStENik>): RecognitionListener {
        return object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                scope.trySend(SpeechRecognitionResultStENik.Ready)
            }

            override fun onBeginningOfSpeech() {
                scope.trySend(SpeechRecognitionResultStENik.Speaking)
            }

            override fun onEndOfSpeech() {
                scope.trySend(SpeechRecognitionResultStENik.EndOfSpeech)
            }

            override fun onResults(results: Bundle?) {
                val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                if (text != null) scope.trySend(
                    SpeechRecognitionResultStENik.Success(
                        text = text,
                        language = language
                    )
                )
                scope.close() // Закрываем Flow после получения окончательного результата
            }

            override fun onError(error: Int) {
                val message = getErrorText(error)
                scope.trySend(SpeechRecognitionResultStENik.Error(message))
                scope.close(Exception(message)) // Отправка ошибки и закрытие Flow
            }

            // ... (onPartialResults, onRmsChanged, onBufferReceived, onEvent)
            override fun onPartialResults(partialResults: Bundle?) { /* ... */
            }

            override fun onRmsChanged(rmsdB: Float) { /* ... */
            }

            override fun onBufferReceived(buffer: ByteArray?) { /* ... */
            }

            override fun onEvent(eventType: Int, params: Bundle?) { /* ... */
            }
        }
    }

    private fun createRecognizerIntent(locale: String): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            // модель для свободного распознавания
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //  язык
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            // Ключ для предпочтения онлайн-распознавания
            // Устанавливая false, говорим системе НЕ предпочитать офлайн-сервис,
            // тем самым давая приоритет онлайн-сервису, если он доступен.
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false)
            // Дополнительные параметры
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    // ... (функция getErrorText)
    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Ошибка записи звука"
            SpeechRecognizer.ERROR_CLIENT -> "Ошибка на стороне клиента"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Недостаточно разрешений"
            SpeechRecognizer.ERROR_NETWORK -> "Сетевая ошибка"
            SpeechRecognizer.ERROR_NO_MATCH -> "Нет совпадений"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Сервис занят"
            SpeechRecognizer.ERROR_SERVER -> "Ошибка сервера"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Таймаут речи (пользователь не говорил)"
            else -> "Неизвестная ошибка: $errorCode"
        }
    }

}








