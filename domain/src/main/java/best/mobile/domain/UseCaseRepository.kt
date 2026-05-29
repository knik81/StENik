package best.mobile.domain

import best.mobile.data.repositoty.RepositoryMain
import best.mobile.entities.ResultStENik
import best.mobile.entities.SettingsStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.VocabularyStENik
import com.google.firebase.auth.FirebaseAuth

class UseCaseRepository(
    private val repositoryMain: RepositoryMain
) {
    val textStENikListFlow = repositoryMain.textStENikListFlow
    suspend fun getTextStENikList() = repositoryMain.getTextStENikList()

    suspend fun getVocabularyStENikList() = repositoryMain.getVocabularyStENikList()

    val vocabularyStENikListFlow = repositoryMain.vocabularyStENikListFlow

    fun <T>saveTextStenikEntityInFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        repositoryMain.saveInFireStore(auth){resultStENik ->
            callBack(resultStENik)
        }
    }

    suspend fun <T>loadFromExcelFile(absolutePath: String): ResultStENik<T> {
        return repositoryMain.loadFromExcelFile(absolutePath)
    }


    fun getSettings(): SettingsStENik = repositoryMain.getSettings()

    fun putSettings(settingsStENik: SettingsStENik) {
        repositoryMain.putSettings(settingsStENik)
    }

    suspend fun saveInDataBase(textStENikList: List<TextStENik>) {
        repositoryMain.saveTextStENikList(textStENikList)
    }


    suspend fun saveVocabulary(newVocabularyStENikList: List<VocabularyStENik>) =
        repositoryMain.saveVocabulary(newVocabularyStENikList = newVocabularyStENikList)

    suspend fun <T>loadFromFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        repositoryMain.loadFromFireStore(auth = auth)
        { resultStENik ->
            callBack(resultStENik) }
    }
}