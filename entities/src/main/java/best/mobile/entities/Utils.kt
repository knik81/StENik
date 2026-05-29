package best.mobile.entities

import kotlinx.serialization.json.Json
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

object Utils {
    const val TAG_STENIK = "StENik_1"
    const val NAME_1 = "StENik"
    const val PAUSE_PLAY_TTS = "PAUSE_PLAY_TTS"
    const val NOTIFICATION_CHANNEL_ID = "serviceTTS_channel"
    const val NOTIFICATION_ID = 77
    const val TEXT_STENIK = "TextStENik"
    const val LANGUAGE_LIST = "languageList"
    const val JSON_ARRAY_STRING_TO_OTHER_SCREEN = "jsonStringArray_to_other_screen"
    val ERROR_AUTH_ID_LIST = listOf(1, 3, 5, 7, 9)
    const val TEMPORARY_ID = -99999999
    const val INIT_ID = -777777
    const val MULTIPLAY = 5
    const val VARIANTS_TEST_MIN = 2f
    const val VARIANTS_TEST_MAX = 10f
    const val VARIANTS_TEST_STEPS = 7
    const val REPEAT_MIN = 1f
    const val REPEAT_MAX = 10f
    const val REPEAT_STEPS = 9
    const val MIC_TIME_MIN = 3f
    const val MIC_TIME_MAX = 10f
    const val MIC_STEPS = 6
    const val SPEED_MIN = 0.5f
    const val SPEED_MAX = 3f
    const val SPEED_STEPS = 9
    const val DELAY_FOR_AWAIT_TTS = 150L
    const val DELAY_FOR_PAUSE_TTS = 1000L
    const val UTTERANCE_ID = "StENik_id"
    const val DELAY_MIN = 0f
    const val DELAY_MAX = 4000f
    const val DELAY_STEPS = 7
    const val WAKE_LOCK_TIME = 200 * 60 * 1000L //200 минут
    const val COLLECTION_NAME = "Data"
    const val COLLECTION_NAME_EXTRA = "Words"


    fun List<VocabularyStENik>.getSelectedVocabularyNameList() =
        this.filter { it.isSelected }.map { it.name }
            .distinct()


    fun List<TextStENik>.textStENikListToJsonStringArray(): Array<String> {
        val jsonStringList = mutableListOf<String>()
        this.forEach { textStENik ->
            jsonStringList.add(Json.encodeToString(textStENik))
        }
        return jsonStringList.toTypedArray()
    }


    fun Array<String>.jsonStringArrayToTextStENikList(): List<TextStENik> {
        val textStENikList = mutableListOf<TextStENik>()
        this.forEach { jsonString ->
            val textStENik = Json.decodeFromString<TextStENik>(jsonString)
            textStENik.textMutableState = textStENik.text
            textStENikList.add(textStENik)
        }
        return textStENikList.toList()
    }

    /*
    fun textStENikListToJsonStringArray(textStENikList: List<TextStENik>): Array<String> {

        val jsonStringList = mutableListOf<String>()
        textStENikList.forEach { textStENik ->
            jsonStringList.add(Json.encodeToString(textStENik))
        }
        return jsonStringList.toTypedArray()
    }

     */


    /*
    fun jsonStringArrayToTextStENikList(jsonStringArray: Array<String>): List<TextStENik> {

        val textStENikList = mutableListOf<TextStENik>()
        jsonStringArray.forEach { jsonString ->
            val textStENik = Json.decodeFromString<TextStENik>(jsonString)
            textStENik.textMutableState = textStENik.text
            textStENikList.add(textStENik)
        }
        return textStENikList.toList()
    }


     */

    /*
    fun jsonStringListToTextStENikList(jsonStringList: MutableList<String>): List<TextStENik> {

        val textStENikList = mutableListOf<TextStENik>()
        jsonStringList.forEach { jsonString ->
            val textStENik = Json.decodeFromString<TextStENik>(jsonString)
            textStENik.textMutableState = textStENik.text
            textStENikList.add(textStENik)
        }
        return textStENikList.toList()
    }

     */

    fun List<TextStENik>.getTextStENikListList(): MutableList<List<TextStENik>> {

        val idList = this.map { it.id }.distinct().sortedBy { it }

        val textStENikListList =
            mutableListOf<List<TextStENik>>()
        idList.forEach { id ->
            val textStENikList =
                this.filter { it.id == id }.sortedBy { it.language }
            textStENikListList.add(textStENikList)
        }
        return textStENikListList

    }

    /*
        fun getTextStENikListList(setTextStENikListFlow: List<TextStENik>): List<List<TextStENik>> {

            val idList = setTextStENikListFlow.map { it.id }.distinct().sortedBy { it }

            val textStENikListList =
                mutableListOf<List<TextStENik>>()
            idList.forEach { id ->
                val textStENikList =
                    setTextStENikListFlow.filter { it.id == id }.sortedBy { it.language }
                textStENikListList.add(textStENikList)
            }
            return textStENikListList

        }

     */


    fun generateNormal(
        //mean: Double,
       // sigma: Int,
        items: Int,
        setTextStENikLangMainList: MutableList<TextStENik>,
        distinct: Boolean = false,
        //single: Boolean = false
    ): MutableList<TextStENik> {

        //Первоначальный список
        val startTextStENikLangMainListFunction: MutableList<TextStENik> =
            setTextStENikLangMainList

        //Итоговый список, в который будут добавляться слова по нормальному распределению
        val finishTextStENikLangMainListFunction = mutableListOf<TextStENik>()

        val maxLevel = setTextStENikLangMainList.maxByOrNull { it.level }?.level ?: 1
        val sigmaSmart = maxLevel / 3.0f
        var counter = 0

        //цикл по полученному списку.
        //Решил его создать в 5 раз больше чем начальный список
        while (counter < items) {//startTextStENikLangMainListFunction.lastIndex * items) {

            var textStENikLangMain: TextStENik?
            counter++

            //Расчет случайного числа, пока с его помощь не выберется значение из списка
            do {
                var u1: Double

                do {
                    u1 = Random.nextDouble()
                } while (u1 == 0.0) // Для избежания log(0)
                val u2: Double = Random.nextDouble()

                val z = sqrt(-2.0 * ln(u1)) * cos(2.0 * PI * u2)

                val normal = kotlin.math.abs(sigmaSmart * z)
                //Log.d(TAG_STENIK, maxNormalLevel.toString())

                /*
                //Список слов c основным языком у которых уровень меньше нормального распределения
                val textStENikLangMainListFunctionIncludeNormalLevel =
                    startTextStENikLangMainListFunction.filter { it.level <= maxNormalLevel }.sortedBy { it.level }

                    //Случайный выбор одного слова из созданного списка
                textStENikLangMain =
                    textStENikLangMainListFunctionIncludeNormalLevel.randomOrNull()
                 */

                textStENikLangMain =
                    startTextStENikLangMainListFunction.filter { it.level <= normal }
                        .randomOrNull()


            } while (textStENikLangMain == null)

            //уменьшение полученного списка
            if (distinct)
                startTextStENikLangMainListFunction.remove(textStENikLangMain)

            //добавление слова в итоговый список
            finishTextStENikLangMainListFunction.add(textStENikLangMain)


        }

        return finishTextStENikLangMainListFunction
    }


}
