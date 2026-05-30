package best.mobile.stenik.ui.screens.vocabulary

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import best.mobile.domain.UseCaseRepository
import best.mobile.entities.ResultStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.TEMPORARY_ID
import best.mobile.entities.Utils.getTextStENikListList
import best.mobile.entities.Utils.textStENikListToJsonStringArray
import best.mobile.entities.VocabularyStENik
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ViewModelVocabularyMain(
    private val useCaseRepository: UseCaseRepository
) : ViewModel() {

    init {
        //подписка на слова словарь из базы данных
        viewModelScope.launch(Dispatchers.IO) {
            //Log.d(TAG_STENIK, "init ViewModelVocabularyText")
            useCaseRepository.textStENikListFlow.collect { textStENikList ->
                _textStENikListList.clear()
                _textStENikListList.addAll(textStENikList.getTextStENikListList())
                textStENikListListNotFiltered.clear()
                textStENikListListNotFiltered.addAll(textStENikList.getTextStENikListList())
            }
        }

        //подписка на название словарей из базы данных
        viewModelScope.launch(Dispatchers.IO) {
            useCaseRepository.vocabularyStENikListFlow.collect { getVocabularyStENikList ->
                vocabularyStENikList.clear()
                vocabularyStENikList.addAll(getVocabularyStENikList.distinct())
            }
        }
    }

    private val textStENikListListNotFiltered = mutableStateListOf<List<TextStENik>>()
    private val _textStENikListList = mutableStateListOf<List<TextStENik>>()
    val textStENikListList
        get() = _textStENikListList

    val searchText = mutableStateOf("")

    private val vocabularyStENikList = mutableStateListOf<VocabularyStENik>()

    //отображение иконки поиска или панели с данными для поиска
    val showSearchIcon = mutableStateOf(true)

    /*private val _vocabulary = mutableStateOf("")
    val vocabulary
        get() = _vocabulary

     */


    //фильтр по тексту
    fun filteredTextStENikListList(setTextForSearch: String) {
        _textStENikListList.clear()
        if (setTextForSearch != "") {
            textStENikListListNotFiltered.forEach { textStENikList ->
                val findTextStENikList = textStENikList.firstOrNull { textStENik ->
                    textStENik.text.contains(
                        setTextForSearch,
                        ignoreCase = true
                    )
                }
                if (findTextStENikList != null)
                    _textStENikListList.add(textStENikList)
            }
        } else {
            _textStENikListList.addAll(textStENikListListNotFiltered)
        }
        // Log.d(TAG_STENIK, "findListList   " + findListList)
    }


    fun getNewEmptyTextStENikList(): Array<String> {


        val settings = useCaseRepository.getSettings()
        val newEmptyTextStENikList = mutableListOf<TextStENik>()
        settings.getLanguageList().forEach { languageStENik ->
            newEmptyTextStENikList.add(
                TextStENik(
                    id = TEMPORARY_ID,
                    language = languageStENik,
                    vocabulary = vocabularyStENikList.firstOrNull { it.isSelected }?.name
                        ?: settings.vocabularyTextDefault,
                    text = "",
                    level = 0,
                    idInit = ""
                )
            )
        }
        val jsonStringArray =
            newEmptyTextStENikList.textStENikListToJsonStringArray()
        return jsonStringArray
    }

    fun <T> saveTextStenikEntityInFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        useCaseRepository.saveTextStenikEntityInFireStore(
            auth
        ) { resultStENik ->
            callBack(resultStENik)
        }
    }

    fun <T> loadFromFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            useCaseRepository.loadFromFireStore(auth = auth)
            { resultStENik ->
                callBack(resultStENik)
            }
        }
    }


    fun <T> loadFromExcelFile(
        context: Context,
        uri: Uri,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val resulTempFiletStENik = copyUriToTempFile(context = context, uri = uri)

            when (resulTempFiletStENik) {
                is ResultStENik.Success -> {
                    val resultStENik: ResultStENik<T> =
                        useCaseRepository.loadFromExcelFile(resulTempFiletStENik.data.absolutePath)
                    (Dispatchers.Main) {
                        callBack(resultStENik)
                    }
                }

                is ResultStENik.Error -> callBack(resulTempFiletStENik)

            }
        }
    }


    //Создание копии Excel файла для дальнейшего его чтения в data слое
    private suspend fun copyUriToTempFile(context: Context, uri: Uri): ResultStENik<File> {
        return withContext(Dispatchers.IO) {
            try {
                // Метод автоматически генерирует уникальное имя файла в папке кэша
                val tempFile = File.createTempFile("excel_import_", ".xlsx", context.cacheDir)

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                //Возвращаем файл
                ResultStENik.Success(tempFile)
            } catch (e: Exception) {
                // Возвращаем ошибку
                ResultStENik.Error(e.message ?: "File is error")
            }
        }
    }

}