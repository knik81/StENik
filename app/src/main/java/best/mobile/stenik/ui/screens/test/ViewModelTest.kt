package best.mobile.stenik.ui.screens.test

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import best.mobile.domain.UseCaseRepository
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.generateNormal
import best.mobile.entities.Utils.getSelectedVocabularyNameList
import best.mobile.entities.Utils.getTextStENikListList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelTest(
    private val useCaseRepository: UseCaseRepository
) : ViewModel() {

    //Список словарей для фильтра
    private val vocabularyNameList = mutableListOf<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            vocabularyNameList.addAll(
                useCaseRepository.getVocabularyStENikList().getSelectedVocabularyNameList()
            )
        }

    }

    private val settings = useCaseRepository.getSettings()
    private val languageList = settings.getLanguageList()

    //Список из базы данных
    private val textStENikList = mutableListOf<TextStENik>()

    //Образец слова
    val textStENikLanguageMain = mutableStateOf<TextStENik?>(null)

    //Список вариантов для выбора
    val textStENikListListWithoutLanguageMain = mutableStateListOf<List<TextStENik>>()


    fun toCheck(
        setId: Int,
        textCorrect: String,
        textIncorrect: String,
        next: Boolean = false,
        delay: Long = 500,
        showToast: (String, Boolean) -> Unit
    ) {

        viewModelScope.launch {
            var newLevel: Int = textStENikLanguageMain.value?.level ?: 0
            val id = textStENikLanguageMain.value?.id

            if (id == setId) {
                newLevel++

            } else {
                if (newLevel > 0)
                    newLevel--
            }

            val textStENikListToSave = mutableListOf<TextStENik>()

            textStENikList.filter { it.id == id }.forEach { getTextStENik ->
                textStENikListToSave.add(
                    TextStENik(
                        id = getTextStENik.id,
                        language = getTextStENik.language,
                        vocabulary = getTextStENik.vocabulary,
                        text = getTextStENik.text,
                        level = newLevel,
                        idInit = getTextStENik.idInit
                    )
                )
            }

            useCaseRepository.saveInDataBase(textStENikListToSave)
            textStENikLanguageMain.value =
                textStENikListToSave.firstOrNull { textStENik -> textStENik.language == settings.languageMain }

            if (id == setId || next) {

                showToast("$textCorrect $newLevel", true)
                //задержка перед сменой текста для зелёного цвета
                viewModelScope.launch {
                    delay(delay)
                    updateTextStENikListListWithoutLanguageMain()
                }
            } else {
                showToast("$textIncorrect $newLevel", false)
            }
        }
    }

    suspend fun updateTextStENikListListWithoutLanguageMain() {

        textStENikList.clear()
        //if (settings.textOnlyDefaultVocabulary)
        textStENikList.addAll(
            useCaseRepository.getTextStENikList()
                .filter { it.vocabulary in vocabularyNameList })//== settings.vocabularyTextDefault })
        //else textStENikList.addAll(useCaseRepository.getTextStENikList())


        if (textStENikList.lastIndex >= settings.amountVariantsForTest) {
            //получить список id с нормальным распределениями
            val idListNormal = generateNormal(
                //mean = 0.0,
                //sigma = settings.sigma,
                items = settings.amountVariantsForTest,
                distinct = true,
                setTextStENikLangMainList = textStENikList.filter { textStENik ->
                    textStENik.language == settings.languageMain
                }.toMutableList()
            ).map { it.id }


            //Список на всех языках с нормальным распределениями
            val textStENikListNormal = textStENikList.filter { textStENik ->
                textStENik.language in languageList && textStENik.id in idListNormal
            }

            //Список со списками на всех языках с нормальным распределениями
            val textStENikListListNormal = textStENikListNormal.getTextStENikListList()
            //getTextStENikListList(textStENikListNormal).toMutableList()


            //Одно слово на главном языке из случайного списка
            textStENikLanguageMain.value = textStENikListListNormal.random()
                .firstOrNull { textStENikList1 -> textStENikList1.language == settings.languageMain }


            textStENikListListWithoutLanguageMain.clear()
            //добавление слов для вариантов
            textStENikListListNormal.forEach { getTextStENikList ->
                textStENikListListWithoutLanguageMain
                    .add(getTextStENikList.filter { getTextStENikList ->
                        getTextStENikList.language != settings.languageMain
                    })
            }
            //Log.d(TAG_STENIK, temp.toString())
        }
    }
}