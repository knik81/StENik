package best.mobile.data.repositoty

import best.mobile.data.entity.TextStENikEntity
import best.mobile.data.entity.VocabularyStENikEntity
import best.mobile.data.repositoty.room.StENikDatabase
import best.mobile.entities.LanguageStENik
import best.mobile.entities.TextStENik
import best.mobile.entities.Utils.INIT_ID
import best.mobile.entities.Utils.TEMPORARY_ID
import best.mobile.entities.VocabularyStENik
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class RepositoryROOM(
    private val stENikDatabase: StENikDatabase
) {

    private val stENikDao = stENikDatabase.getDao

    //заготовка для функции вставки записи
    private var newId = 0
    private var newInitId = 0


    // Загрузка слов
    fun getTextStENikListFlow(): Flow<List<TextStENik>> {
        return stENikDao.getAllTextStENikEntityListFlow().map { textStENikList ->

            //newId - это заготовка для функции вставки записи
            //Определение максимума для увелечения на +1.
            if (textStENikList.isNotEmpty()) {
                newId = textStENikList.maxBy { it.id }.id + 1
                newInitId = textStENikList.minBy { it.id }.id - 1
            }
            if (newId < 0) newId = 0


            //Мэппинг из TextStENikEntity в TextStENik
            textStENikList.map { entity ->
                //Log.d(TAG_STENIK , "get1   " + entity.toString())
                TextStENik(
                    id = entity.id,
                    language = LanguageStENik.EMPTY.stringToLanguageStENik(entity.language),
                    vocabulary = entity.vocabulary,
                    text = entity.text,
                    level = entity.level,
                    idInit = entity.idInit
                )
            }
        }
    }


    suspend fun saveStENikListFromExcelFile(textStENikMapExcel: MutableMap<String, MutableList<TextStENik>>) {

        textStENikMapExcel.forEach { (_, setTextStENikListExcel) ->
            //загрузить в него level из базы данных
            // список из БД
            val initTextStENikFromDBList = getTextStENikList().filter { it.idInit != "" }


            //получить стартовый id для начального списка
            val startId =
                if (initTextStENikFromDBList.isNotEmpty())
                    initTextStENikFromDBList.maxBy { it.id }.id
                else 0


            //  !!!! Сортировка для присвоения id. Это важно!!!
            val textStENikListExcel = setTextStENikListExcel.sortedBy { it.idInit }
            var idInit = "temp"
            var countID = startId

            //получить заготовленный список слов
            //идентификация по initID
            //присвоить каждому существующему id из базы данных - это по сути перезапись
            // и изменить уровень, взятый из базы данных
            textStENikListExcel.forEach { textStENikExcel ->
                var check = false
                initTextStENikFromDBList.forEach { textStENikFromDB ->
                    if (textStENikExcel.idInit == textStENikFromDB.idInit) {
                        textStENikExcel.level = textStENikFromDB.level
                        textStENikExcel.id = textStENikFromDB.id
                        check = true
                    }
                }

                //Ветка для присвоения id новым записям
                // textStENikListExcel должен быть отсортирован по idInit
                if (!check)
                    if (textStENikExcel.idInit != idInit) {//проверка изменения idInit
                        countID++
                        idInit = textStENikExcel.idInit
                        textStENikExcel.id = countID
                    } else
                        textStENikExcel.id = countID
            }

            //сохраним этот заготовленный список слов
            saveTextStENikList(textStENikListExcel)
        }

        //сохраним словари
        prepareAndSaveVocabularyStENikList(getTextStENikList())

    }

    suspend fun getTextStENikList(): List<TextStENik> {
        return stENikDao.getAllTextStENikEntityList().map { textStENikEntity ->
            //Мэппинг из textStENikEntity в TextStENik
            TextStENik(
                id = textStENikEntity.id,
                language = LanguageStENik.EMPTY.stringToLanguageStENik(textStENikEntity.language),
                vocabulary = textStENikEntity.vocabulary,
                text = textStENikEntity.text,
                level = textStENikEntity.level,
                idInit = textStENikEntity.idInit
            )
        }
    }

    suspend fun prepareAndSaveVocabularyStENikList(textStENikList: List<TextStENik>) {
        //получим словари
        val vocabularyStENikList = mutableListOf<VocabularyStENik>()
        textStENikList.map { it.vocabulary }.distinct().forEach {
            vocabularyStENikList.add(VocabularyStENik(name = it, isSelected = true))
        }
        //сохраним словарь в базе данных
        if (vocabularyStENikList.isNotEmpty())
            saveVocabularyStENikList(vocabularyStENikList)
    }


    //Сохранение слов
    suspend fun saveTextStENikList(textStENikList: List<TextStENik>) {

        //перед записью удаление записей с одинаковыми id
        val isList = textStENikList.map { textStENik -> textStENik.id }.distinct()
        isList.forEach { id ->
            stENikDao.deleteTextStENikEntityById(id = id)
        }

        //перед записью начального текста его удаление по idInit
        val isInitList =
            textStENikList.filter { it.idInit != "" }.map { textStENik -> textStENik.idInit }
                .distinct()
        isInitList.forEach { idInit ->
            stENikDao.deleteTextStENikEntityByIdInit(idInit = idInit)
        }

        val textStENikEntityList = mutableListOf<TextStENikEntity>()

        //создание списка TextStENikEntity
        textStENikList.filter { it.text != "" }.forEach { textStENik ->
            textStENikEntityList.add(
                TextStENikEntity(
                    //для новых записей использую TEMPORARY_ID, для начальных записей - INIT_ID. Если это он, то подменяю временный id на newId
                    id =
                        when (textStENik.id) {
                            TEMPORARY_ID -> newId
                            INIT_ID -> newInitId
                            else -> textStENik.id
                        },
                    language = LanguageStENik.EMPTY.languageStENikToString(textStENik.language),
                    vocabulary = textStENik.vocabulary,
                    text = textStENik.text,
                    level = textStENik.level,
                    idInit = textStENik.idInit
                )
            )
            //Log.d(TAG_STENIK, "save   " + textStENikEntity.toString())
        }
        //Запись
        textStENikEntityList.forEach { setTextStENikEntity ->
            stENikDao.insertTextStENikEntity(setTextStENikEntity)
        }

    }

    //Получения словарей - flow
    fun getVocabularyStENikListFlow(): Flow<List<VocabularyStENik>> {
        return stENikDao.getAllVocabularyStENikFlow().map { vocabularyStENikList ->

            vocabularyStENikList.map { entity ->
                //Log.d(TAG_STENIK , "get1   " + entity.toString())
                VocabularyStENik(
                    name = entity.name,
                    isSelected = entity.isSelected
                )
            }
        }
    }

    //Получения словарей
    suspend fun getVocabularyStENikList(): List<VocabularyStENik> {
        return stENikDao.getAllVocabularyStENikList().map { vocabularyStENikEntity ->
            //Мэппинг из vocabularyStENikEntity в VocabularyStENik
            VocabularyStENik(
                name = vocabularyStENikEntity.name,
                isSelected = vocabularyStENikEntity.isSelected
            )
        }
    }

    //Сохранение словаря
    suspend fun saveVocabularyStENikList(vocabularyStENikList: List<VocabularyStENik>) {
        //перед записью удаление
        stENikDao.deleteVocabularyStENikEntity()
        //Запись
        vocabularyStENikList.filter { it.name != "" }.forEach { vocabularyStENik ->
            val vocabularyStENikEntity = VocabularyStENikEntity(
                name = vocabularyStENik.name,
                isSelected = vocabularyStENik.isSelected

            )
            //Log.d(TAG_STENIK, "save   " + textStENikEntity.toString())
            stENikDao.insertVocabularyStENikEntity(vocabularyStENikEntity)
        }
    }
}