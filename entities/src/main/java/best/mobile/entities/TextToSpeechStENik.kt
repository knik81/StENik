package best.mobile.entities


import best.mobile.entities.Utils.MULTIPLAY
import best.mobile.entities.Utils.generateNormal



class TextToSpeechStENik(
    private val textStENikList: List<TextStENik>
) {

    //Возвращает список со списком с типом TextToSpeechStENikItem
    fun getTextForTTS(
        languageList: MutableList<LanguageStENik>,
        sortedBy: SortedTextBy,
        //sigma: Int
    ): MutableList<MutableList<TextStENikSpeech>> {

        if (languageList.isEmpty())
            languageList.add(LanguageStENik.RUSSIAN)
        val textStENikLangMainList: MutableList<TextStENik> =
            textStENikList.filter { it.language == languageList.first() }
                .toMutableList()//список слов основного языка


        when (sortedBy) {//сортировка списка слов у основного языка
            SortedTextBy.BY_ID -> textStENikLangMainList.sortBy { it.id }
            SortedTextBy.BY_LEVEL -> textStENikLangMainList.sortBy { it.level }
            SortedTextBy.BY_SMART -> {

                //получить список с нормальным распределение
                //мат ожидание = 0, сигма из настроек
                val textStENikLangMainListNormal = generateNormal(
                    //mean = 0.0,
                    //sigma = sigma,
                    items = textStENikLangMainList.lastIndex * MULTIPLAY,//вернется список больше в MULTIPLAY раз
                    setTextStENikLangMainList = textStENikLangMainList
                )

                //замена списка с учетом нормального распределения
                textStENikLangMainList.clear()
                textStENikLangMainList.addAll(textStENikLangMainListNormal)
            }

            else -> textStENikLangMainList.sortBy { it.text }
        }


        val finalListForTTS =
            mutableListOf<MutableList<TextStENikSpeech>>() // финальный отсортированный список. Его элемент - это "внутренний" список слов в нужном порядке языка

        //Проход по отсортированному списку. Поиск перевода.
        // Заполнение "внутреннего" списка с типом TextToSpeechStENikItem.
        // И добавление этого списка в финальный список
        textStENikLangMainList.forEach { textStENikLangMain ->

            val interList =
                mutableListOf<TextStENikSpeech>() // Внутренний список с типом TextToSpeechStENikItem

            val textStENikSpeechLangMain = TextStENikSpeech(
                id = textStENikLangMain.id,
                text = textStENikLangMain.text,
                locale = textStENikLangMain.language.locale,
                vocabulary = textStENikLangMain.vocabulary
            )

            interList.add(textStENikSpeechLangMain)//Заполнение "внутреннего" списка основным языком. Он должен быть первым

            //Проход по всем дополнительным языкам. Поиск по id перевода на каждом дополнительном языке.
            //Добавление найденного перевода во "вторичный" список
            languageList.forEachIndexed { index, languageItem ->
                if (index > 0) {//исключили основной язык, он всегда первый в languageList.
                    val textStENikLangExtra = //поиск  по id перевода на нужном языке
                        textStENikList.filter { it.id == textStENikSpeechLangMain.id }
                            .firstOrNull { it.language == languageItem }

                    if(textStENikLangExtra != null) {
                        val textStENikSpeechLangExtra = TextStENikSpeech(
                            id = textStENikLangExtra.id,
                            text = textStENikLangExtra.text,
                            locale = textStENikLangExtra.language.locale,
                            vocabulary = textStENikLangMain.vocabulary
                        )
                        interList.add(textStENikSpeechLangExtra)//Добавление во "внутренний" список перевода.
                    }
                }
            }
            finalListForTTS.add(interList)//добавление вторичного списка в финальный список.
        }
        return finalListForTTS
    }

/*
    //возвращает список слов с нормальным распределением
    private fun generateNormal(
        mean: Double,
        sigma: Int,
        setTextStENikLangMainList: MutableList<TextStENik>
    ): MutableList<TextStENik> {

        //Первоначальный список
        val startTextStENikLangMainListFunction: MutableList<TextStENik> =
            setTextStENikLangMainList

        //Итоговый список, в который будут добавляться слова по нормальному распределению
        val finishTextStENikLangMainListFunction = mutableListOf<TextStENik>()

        var counter = 0

        //цикл по полученному списку.
        //Решил его создать в 5 раз больше чем начальный список
        while (counter < startTextStENikLangMainListFunction.lastIndex * 5) {

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

                val maxNormalLevel = kotlin.math.abs(mean + sigma * z)
                Log.d(TAG_STENIK, maxNormalLevel.toString())

                //Список слов c основным языком у которых уровень меньше нормального распределения
                val textStENikLangMainListFunctionIncludeNormalLevel =
                    startTextStENikLangMainListFunction.filter { it.id <= maxNormalLevel }

                //Случайный выбор одного слова из созданного списка
                textStENikLangMain =
                    textStENikLangMainListFunctionIncludeNormalLevel.randomOrNull()

            } while (textStENikLangMain == null)

            //уменьшение полученного списка
            //startTextStENikLangMainListFunction.remove(textStENikLangMain)
            //добавление слова в итоговый список
            finishTextStENikLangMainListFunction.add(textStENikLangMain)
        }

        return finishTextStENikLangMainListFunction
    }

 */

}

