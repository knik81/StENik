package best.mobile.entities


sealed class SpeechRecognitionResultStENik {
    data class Success(val text: String,val language: LanguageStENik) : SpeechRecognitionResultStENik()
    data class Partial(val text: String) : SpeechRecognitionResultStENik()
    data class Error(val message: String) : SpeechRecognitionResultStENik()
    object Ready : SpeechRecognitionResultStENik()
    object Speaking : SpeechRecognitionResultStENik()
    object EndOfSpeech : SpeechRecognitionResultStENik()
    object IsFinish : SpeechRecognitionResultStENik()
}