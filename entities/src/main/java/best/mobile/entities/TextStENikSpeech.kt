package best.mobile.entities


import java.util.Locale


data class TextStENikSpeech(
    override var id: Int = 0,
    override var text: String,
    val locale: Locale,
    override var vocabulary: String,
    override var level: Int = 0,
    //override var idInit: String
): TextStENikInterface

