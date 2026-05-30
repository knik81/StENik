package best.mobile.stenik.ui.screens.vocabulary

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import best.mobile.domain.UseCaseRepository
import best.mobile.entities.VocabularyStENik
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelVocabularyNameCustom(
    private val useCaseRepository: UseCaseRepository,
) : ViewModel() {

    init {
        //подписка на словарь из базы данных
        viewModelScope.launch(Dispatchers.IO) {
            useCaseRepository.vocabularyStENikListFlow.collect { vocabularyStENikList ->
                vocabularyList.clear()
                vocabularyList.addAll(vocabularyStENikList)
            }
        }
    }

    //val settings = mutableStateOf(useCaseRepository.getSettings())

    val vocabularyList =
        mutableStateListOf<VocabularyStENik>()

/*
    private val _vocabulary = mutableStateOf("")
    val vocabulary
        get() = _vocabulary

 */


    fun saveVocabularyList(newVocabularyStENikList: List<VocabularyStENik>) {
        viewModelScope.launch(Dispatchers.IO) {
            useCaseRepository.saveVocabulary(newVocabularyStENikList = newVocabularyStENikList)
        }
    }

}