package best.mobile.entities


import kotlinx.coroutines.flow.Flow

interface SpeechRecognizerStENikInterface {
    // Возвращает Flow для асинхронной передачи результатов/состояний
    fun startListening(setLanguage: LanguageStENik): Flow<SpeechRecognitionResultStENik>
    fun stopListening()
}