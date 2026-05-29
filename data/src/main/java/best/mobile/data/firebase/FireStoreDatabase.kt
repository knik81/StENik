package best.mobile.data.firebase

import best.mobile.data.entity.TextStENikEntity
import best.mobile.entities.LanguageStENik
import best.mobile.entities.ResultStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.COLLECTION_NAME
import best.mobile.entities.Utils.COLLECTION_NAME_EXTRA
import best.mobile.entities.Utils.NAME_1
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FireStoreDatabase {
    val fs = Firebase.firestore

    fun <T>saveInFireStore(
        textStENikList: List<TextStENik>,
        userId: String,
        returnResultStENik: (ResultStENik<T>) -> Unit
    ){
        fs.collection(NAME_1)
            .document(userId)
            .collection(COLLECTION_NAME)
            .document(COLLECTION_NAME_EXTRA)
            .set(textStENikToTextStENikEntityMap(textStENikList).toMap())
            .addOnFailureListener { error ->
                returnResultStENik(ResultStENik.Error(error.message ?: "Unknown Error"))
            }
            .addOnSuccessListener {
                returnResultStENik(ResultStENik.Success("Ok" as T))
            }

    }


    suspend fun loadFromFireStore(
        userId: String,
        returnTextTextStenikList: (ResultStENik<List<TextStENik>>) -> Unit//(List<TextStENik>) -> Unit
    ) {
        val textTextStenikList = mutableListOf<TextStENik>()

        val snapshot = fs.collection(NAME_1)
            .document(userId)
            .collection(COLLECTION_NAME)
            .document(COLLECTION_NAME_EXTRA)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    documentSnapshot.data?.forEach { (key, _) ->
                        val rawMap = documentSnapshot.get(key) as? Map<*, *>
                        if (rawMap != null) {

                            //Мэппинг данных в TextStENik
                            textTextStenikList.add(
                                TextStENik(
                                    id = (rawMap["id"] as? Long ?: 0L).toInt(),
                                    language = LanguageStENik.EMPTY.stringToLanguageStENik(
                                        rawMap["language"] as? String ?: "US"
                                    ),
                                    vocabulary = rawMap["vocabulary"] as? String ?: "",
                                    text = rawMap["text"] as? String ?: "",
                                    level = (rawMap["level"] as? Long ?: 0L).toInt(),
                                    idInit = rawMap["idInit"] as? String ?: "",
                                )
                            )
                        }
                    }
                    returnTextTextStenikList(ResultStENik.Success(textTextStenikList))
                }
            }
            .addOnFailureListener {
                returnTextTextStenikList(ResultStENik.Error(it.message ?: "Unknown Error"))
            }.await()// при отсутствии данных snapshot == Null далее есть проверка

        // Проверка на наличие данных в облаке
        if (snapshot.data == null) {
            returnTextTextStenikList(ResultStENik.Error("Not found"))
        }
    }


    private fun textStENikToTextStENikEntityMap(setTextStENikList: List<TextStENik>): Map<String, TextStENikEntity> {
        val textStENikFireBaseMap: MutableMap<String, TextStENikEntity> =
            emptyMap<String, TextStENikEntity>().toMutableMap()

        setTextStENikList.forEach { textStENik ->
            textStENikFireBaseMap["${textStENik.id}-${textStENik.language}"] =
                TextStENikEntity(
                    id = textStENik.id,
                    language = textStENik.language.toString(),
                    vocabulary = textStENik.vocabulary,
                    text = textStENik.text,
                    level = textStENik.level,
                    idInit = textStENik.idInit
                )
            // Log.d(TAG_StENik, textStENikROOM.text)
        }
        return textStENikFireBaseMap
    }


}

