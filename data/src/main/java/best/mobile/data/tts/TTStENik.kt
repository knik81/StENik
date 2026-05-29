package best.mobile.data.tts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import best.mobile.data.repositoty.RepositoryMain
import best.mobile.entities.SortedTextBy
import best.mobile.entities.TextStENikSpeech
import best.mobile.entities.TextToSpeechStENik
import best.mobile.entities.Utils.DELAY_FOR_AWAIT_TTS
import best.mobile.entities.Utils.DELAY_FOR_PAUSE_TTS
import best.mobile.entities.Utils.NAME_1
import best.mobile.entities.Utils.TAG_STENIK
import best.mobile.entities.Utils.UTTERANCE_ID
import best.mobile.entities.Utils.WAKE_LOCK_TIME
import best.mobile.entities.Utils.getSelectedVocabularyNameList
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TTStENik {

    //Создание джоба для того, чтобы можно было его остановить
    var jobTTS: Job? = null
    val pause = mutableStateOf(false)
    var wakeLock: PowerManager.WakeLock? = null

    var tts: TextToSpeech? = null

    fun pauseTTS() {
        pause.value = !pause.value
    }

    fun cancelTTS() {
        Log.d(TAG_STENIK, "stopTTS, ${pause.value}")
        pause.value = false
        if (wakeLock?.isHeld == true)
            wakeLock?.release()
        tts?.shutdown()
        jobTTS?.cancel()
        tts?.stop()
    }

    suspend fun startTTS(
        context: Context,
        repositoryMain: RepositoryMain,
        powerManager: PowerManager? = null,
        cardTextListChange: (MutableList<TextStENikSpeech>) -> Unit,
        setStopTTS: () -> Unit
    ) {
        //Log.d(TAG_STENIK, "startTTS, ${pause.value}")
        if (!pause.value)

            playTTS(
                context = context,
                repositoryMain = repositoryMain,
                powerManager = powerManager,
                cardTextListChange = { textToSpeechStENikItem ->
                    cardTextListChange(textToSpeechStENikItem)
                },
                setStopTTS = { setStopTTS() }
            )
        else
            pause.value = false
    }


    @SuppressLint("ServiceCast")
    private suspend fun playTTS(
        context: Context,
        repositoryMain: RepositoryMain,
        powerManager: PowerManager? = null,
        cardTextListChange: (MutableList<TextStENikSpeech>) -> Unit,
        setStopTTS: () -> Unit
    ) {

        val sortedBy: SortedTextBy = repositoryMain.getSettings().sortedTextBy
        val languageList = repositoryMain.getLanguageList()
        val delayReadText = repositoryMain.getSettings().delayReadText.toLong()
        val repeatListenAmount =
            repositoryMain.getSettings().repeatListenAmount
        val readSpeedLanguageList = listOf(
            repositoryMain.getSettings().readSpeedMainLanguage,
            repositoryMain.getSettings().readSpeedFirstLanguage,
            repositoryMain.getSettings().readSpeedSecondLanguage
        )

        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Log.e(TAG_STENIK, "Проблема 2")
                cancelTTS()
                setStopTTS()
            }
        }

        // Создаем WakeLock (PARTIAL_WAKE_LOCK оставляет CPU включенным при выключенном экране)
        wakeLock = powerManager?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "$NAME_1::playTTSWakelock"
        )


        //Запуск джоба. В нем будет запущен TTS
        coroutineScope {

            //список имен выбранных словарей
            val vocabularyStENikListName =
                repositoryMain.getVocabularyStENikList().getSelectedVocabularyNameList()

            repositoryMain.textStENikListFlow.collect { getTextStENikList ->//getTextStENik()

                val textStENikList = getTextStENikList.toMutableList()

                //Фильтр по словарю
                textStENikList.clear()
                textStENikList.addAll(getTextStENikList.filter { it.vocabulary in vocabularyStENikListName })


                if (!(jobTTS?.isActive ?: false))
                    jobTTS = launch {

                        //От засыпания захватываем блокировку
                        wakeLock?.acquire(WAKE_LOCK_TIME) // Опциональный таймаут WAKE_LOCK_TIME минут

                        run breaking@{
                            repeat(10) {
                                //получить список слов в нужном виде для TTS
                                val finalListForTTS =
                                    TextToSpeechStENik(textStENikList)
                                        .getTextForTTS(
                                            languageList = languageList,
                                            sortedBy = sortedBy
                                        )

                                //Нет текста для прослушивания.
                                if (finalListForTTS.isEmpty()) {
                                    setStopTTS()
                                    cancelTTS()
                                    return@breaking
                                }

                                finalListForTTS.forEachIndexed { _, textToSpeechList ->
                                    //Создание и заполнение текста для карточек на экране
                                    val cardTextList = mutableListOf<TextStENikSpeech>()
                                    textToSpeechList.forEach { textToSpeechItem ->
                                        cardTextList.add(textToSpeechItem)
                                    }

                                    //Калбэк для обновления текста в карточках
                                    cardTextListChange(cardTextList)

                                    repeat(repeatListenAmount) {
                                        textToSpeechList.forEachIndexed { indexItemLanguage, textToSpeechItem ->
                                            var counterForStart = 0
                                            var result = tts?.setLanguage(textToSpeechItem.locale)

                                            //ожидает паузу, которую поставил пользователь
                                            while (pause.value)
                                                delay(DELAY_FOR_PAUSE_TTS)

                                            //Задержка из настроек. Чтобы подумать и предположить перевод
                                            if (indexItemLanguage != 0)
                                                delay(delayReadText)

                                            //Почему-то с первого раза не проходит проверка. Для этого сделал цикл со счетчиком. Нужно разобраться...
                                            while ((result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) && counterForStart < 20) {
                                                counterForStart++
                                                delay(DELAY_FOR_AWAIT_TTS)
                                                result = tts?.setLanguage(textToSpeechItem.locale)
                                                Log.e(
                                                    TAG_STENIK,
                                                    "Проблема с языком $counterForStart"
                                                )
                                                Log.d(TAG_STENIK, "${textToSpeechItem.text} ")
                                            }

                                            if (readSpeedLanguageList.lastIndex >= indexItemLanguage)
                                                tts?.setSpeechRate(readSpeedLanguageList[indexItemLanguage])
                                            else
                                                tts?.setSpeechRate(1.0f)

                                            //ожидает завершение чтения предыдущего слова
                                            while (tts?.isSpeaking ?: false)
                                                delay(DELAY_FOR_AWAIT_TTS)

                                            //Чтение слова
                                            tts?.speak(
                                                textToSpeechItem.text,
                                                TextToSpeech.QUEUE_ADD,
                                                Bundle(),
                                                UTTERANCE_ID
                                            )

                                            delay(DELAY_FOR_AWAIT_TTS)

                                            //ожидает завершение чтения слова
                                            while (tts?.isSpeaking ?: false)
                                                delay(DELAY_FOR_AWAIT_TTS)
                                        }
                                    }
                                }
                            }
                            setStopTTS()
                            cancelTTS()
                        }
                    }
            }
        }
    }
}