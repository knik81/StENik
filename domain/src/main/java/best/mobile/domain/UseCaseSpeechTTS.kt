package best.mobile.domain

import android.content.Context
import best.mobile.data.tts.MainTTS
import best.mobile.entities.LanguageStENik
import best.mobile.entities.TextStENikSpeech

class UseCaseSpeechTTS(
    private val mainTTS: MainTTS
) {
    fun startServiceTTS(context: Context) {
        mainTTS.startServiceTTS(context)
    }

    fun stopTTS(context: Context) {
        mainTTS.stopTTS(context)
    }


    suspend fun startTTS(
        context: Context,
        cardTextListChange: (MutableList<TextStENikSpeech>) -> Unit
    ) {
        mainTTS.startTTS(
            context = context,
            cardTextListChange = { cardTextList ->
                cardTextListChange(cardTextList) },
        )
    }


    fun pauseTTS() {
        mainTTS.pauseTTS()
    }


    val recognitionState = mainTTS.recognitionState

    fun getTextBySpeech(language: LanguageStENik) {
        // Log.d(TAG_STENIK, "useCase - getTextBySpeech")
        mainTTS.getTextBySpeech(language = language)
    }

    fun stopTextBySpeech() {
        mainTTS.stopTextBySpeech()
    }
}