package best.mobile.data.repositoty

import best.mobile.data.firebase.FireStoreDatabase
import best.mobile.data.repositoty.file_excel.ReadExcelFile
import best.mobile.entities.ResultStENik
import best.mobile.entities.SettingsStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.VocabularyStENik
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class RepositoryMain(
    private val repositoryROOM: RepositoryROOM,
    private val repositorySharedPreference: RepositorySharedPreference,
    private val fireStoreDatabase: FireStoreDatabase,
    private val readExcelFile: ReadExcelFile
) : KoinComponent {

    val textStENikListFlow = repositoryROOM.getTextStENikListFlow()

    val vocabularyStENikListFlow = repositoryROOM.getVocabularyStENikListFlow()

    suspend fun saveTextStENikList(textStENikList: List<TextStENik>) {
        repositoryROOM.saveTextStENikList(textStENikList)
    }


    suspend fun saveVocabulary(newVocabularyStENikList: List<VocabularyStENik>) {
        repositoryROOM.saveVocabularyStENikList(newVocabularyStENikList)
    }


    suspend fun getTextStENikList() = repositoryROOM.getTextStENikList()

    suspend fun getVocabularyStENikList() = repositoryROOM.getVocabularyStENikList()


    fun getLanguageList() = getSettings().getLanguageList()

    fun <T> saveInFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null)
            CoroutineScope(Dispatchers.IO).launch {
                val textStENikList = getTextStENikList()
                fireStoreDatabase.saveInFireStore(
                    textStENikList = textStENikList,
                    userId = uid,
                    returnResultStENik = { resultStENik ->
                        callBack(resultStENik)
                    })
            }
    }


    suspend fun <T> loadFromFireStore(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null)
            fireStoreDatabase.loadFromFireStore(
                userId = uid,
                returnTextTextStenikList = { resultStENik ->
                    when (resultStENik) {
                        is ResultStENik.Success -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                //сохранение текста в базе данных
                                saveTextStENikList(resultStENik.data)

                                //сохраним словари
                                repositoryROOM.prepareAndSaveVocabularyStENikList(resultStENik.data)

                                withContext(Dispatchers.Main) {
                                    callBack(ResultStENik.Success("Ok" as T))
                                }
                            }
                        }

                        is ResultStENik.Error -> {
                            callBack(resultStENik)
                        }
                    }
                },
            )
    }


    fun getSettings(): SettingsStENik = repositorySharedPreference.getSettings()


    fun putSettings(settingsStENik: SettingsStENik) {
        repositorySharedPreference.putSettings(settingsStENik)
    }


    suspend fun <T> loadFromExcelFile(absolutePath: String): ResultStENik<T> {

        val resultStENik =
            readExcelFile.getTextStENikList(absolutePath = absolutePath)

        return when (resultStENik) {
            is ResultStENik.Success -> {
                // Данные из Excel - это начальные данные
                // Для их сохранения создал отдельную функцию
                repositoryROOM.saveStENikListFromExcelFile(resultStENik.data)
                ResultStENik.Success("Ок" as T)
            }
            is ResultStENik.Error -> resultStENik
        }
    }


}