package best.mobile.stenik.ui.screens.vocabulary

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import best.mobile.domain.UseCaseRepository
import best.mobile.domain.UseCaseSpeechTTS
import best.mobile.entities.LanguageStENik
import best.mobile.entities.SpeechRecognitionResultStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.jsonStringArrayToTextStENikList
import best.mobile.entities.VocabularyStENik
import best.mobile.stenik.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch

class ViewModelVocabularyTextEdit(
    private val useCaseRepository: UseCaseRepository,
    private val useCaseSpeechTTS: UseCaseSpeechTTS,
    application: Application
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
           // Log.d(TAG_STENIK, "init ViewModelVocabularyTextEdit")

            //загрузка списка словарей из базы данных
            useCaseRepository.vocabularyStENikListFlow.collect { getVocabularyStENikList ->
                vocabularyStENikList.clear()
                vocabularyStENikList.addAll(getVocabularyStENikList.distinct())
            }
        }
        //Log.d(TAG_STENIK, "${_textStENikListForEditScreen}")
    }


    private val settings = useCaseRepository.getSettings()
    private val appContext = application.applicationContext

    private val _vocabulary = mutableStateOf<VocabularyStENik?>(null)
    val vocabulary
        get() = _vocabulary

    //private val _vocabularyStENikListFlow = useCaseRepository.vocabularyStENikListFlow
    val vocabularyStENikList = mutableStateListOf<VocabularyStENik>()

    //Список передан из экрана словаря в экран редактирования. Список содержит все слова с одинаковым id.
    private val _textStENikListForEditScreen = mutableStateListOf<TextStENik>()//<TextStENik>()
    val textStENikListForEditScreen
        get() = _textStENikListForEditScreen


    private val _iconIsEnable = mutableStateOf(true)
    val iconIsEnable
        get() = _iconIsEnable


    fun initDataForEdit(jsonStringArray: Array<String>) {

        // заполнить словарем из настроек
        _vocabulary.value = VocabularyStENik(
            name = settings.vocabularyTextDefault,
            isSelected = false
        )

        // поиск словаря из настроек в списке словаре базы данных
        val isExist =
            vocabularyStENikList.firstOrNull { vocabularyStENik -> vocabularyStENik.name == _vocabulary.value?.name }

        //Если в базе нет словаря из настроек, то создадим и добавляем его в лист
        if (isExist == null) {
            _vocabulary.value =
                VocabularyStENik(name = settings.vocabularyTextDefault, isSelected = false)
            vocabularyStENikList.add(_vocabulary.value!!)
        }

        //Загрузка данных, полученных с другого экрана.
        if (jsonStringArray.isNotEmpty()) {
            //перевод данных в TextStENik из json
            _textStENikListForEditScreen.clear()
            _textStENikListForEditScreen.addAll(jsonStringArray.jsonStringArrayToTextStENikList())

            //вывод в качестве выбранного - словарь из первой полученной записи
            _vocabulary.value = VocabularyStENik(
                name = _textStENikListForEditScreen.firstOrNull()?.vocabulary ?: "",
                isSelected = false
            )
        }
        //Log.d(TAG_STENIK, "${_textStENikListForEditScreen}")
    }


    fun setNewText(languageList: List<LanguageStENik>, newText: String) {
        languageList.forEach { language ->
            _textStENikListForEditScreen.first { it.language == language }.textMutableState =
                newText
        }

    }

    fun setNewVocabulary(newVocabulary: VocabularyStENik) {
        _vocabulary.value = newVocabulary
    }

    fun saveTextStENikListInDataBase(
        onClickBack: () -> Unit
    ) {
        _textStENikListForEditScreen.forEach { _textStENik ->
            _textStENik.text = _textStENik.textMutableState
            _textStENik.vocabulary = vocabulary.value?.name ?: ""
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCaseRepository.saveInDataBase(_textStENikListForEditScreen)
            (Dispatchers.Main){
                onClickBack()
            }
        }
    }


    //проверка разрешения микрофона и запуск распознавания речи
    fun checkPermissionAndStartSpeechRecognizer(
        language: LanguageStENik,
        startLaunch: () -> Unit
    ) {
        //Log.d(TAG_STENIK, "PERMISSION - getTextBySpeech")
        when (PackageManager.PERMISSION_GRANTED) {
            //проверка уведомления
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                viewModelScope.launch {
                    //Log.d(TAG_STENIK, "VM - getTextBySpeech")
                    _iconIsEnable.value = false

                    //запустить распознавания речи
                    useCaseSpeechTTS.getTextBySpeech(language = language)

                    //Приостанавливаем корутину на  секунды
                    //Ожидание речи
                    delay((settings.timeMillsSecForSpeech * 1000L))
                    useCaseSpeechTTS.stopTextBySpeech()
                    _iconIsEnable.value = true

                    //Получение текст из речи
                    useCaseSpeechTTS.recognitionState.collect { recognitionState ->
                        when (recognitionState) {
                            SpeechRecognitionResultStENik.EndOfSpeech -> {}
                            is SpeechRecognitionResultStENik.Error -> {}
                            is SpeechRecognitionResultStENik.Partial -> {}
                            SpeechRecognitionResultStENik.Ready -> {}
                            SpeechRecognitionResultStENik.Speaking -> {}
                            is SpeechRecognitionResultStENik.Success -> {
                                setNewText(
                                    languageList = listOf(recognitionState.language),
                                    newText = recognitionState.text
                                )
                            }

                            SpeechRecognitionResultStENik.IsFinish -> {}
                            null -> {}
                        }
                    }
                }
            }

            else -> {
                Toast.makeText(
                    appContext,
                    appContext.getString(R.string.msg_grantMicrophone),
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(appContext, "", Toast.LENGTH_LONG).show()
                startLaunch()
            }
        }
    }
}