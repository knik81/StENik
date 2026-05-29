package best.mobile.data.repositoty.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import best.mobile.data.entity.TextStENikEntity
import best.mobile.data.entity.VocabularyStENikEntity
import best.mobile.entities.VocabularyStENik
import kotlinx.coroutines.flow.Flow

@Dao
interface StENikDao {

    @Query("SELECT * FROM TextStENikEntity")
    fun getAllTextStENikEntityListFlow(): Flow<List<TextStENikEntity>>

    @Query("SELECT * FROM TextStENikEntity")
    suspend fun getAllTextStENikEntityList(): List<TextStENikEntity>

    @Query("SELECT * FROM TextStENikEntity WHERE id IN (:id)")
    suspend fun getTextStENiEntityById(id: IntArray): List<TextStENikEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTextStENikEntity(vararg textStENikEntity: TextStENikEntity)

    @Query("DELETE FROM TextStENikEntity WHERE id = :id")
    suspend fun deleteTextStENikEntityById(id: Int)

    @Query("DELETE FROM TextStENikEntity WHERE idInit = :idInit")
    suspend fun deleteTextStENikEntityByIdInit(idInit: String)


    @Query("SELECT * FROM VocabularyStENikEntity")
    fun getAllVocabularyStENikFlow(): Flow<List<VocabularyStENikEntity>>

    @Query("SELECT * FROM VocabularyStENikEntity")
    suspend fun getAllVocabularyStENikList(): List<VocabularyStENik>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabularyStENikEntity(vararg vocabularyStENikEntity: VocabularyStENikEntity)

    @Query("DELETE FROM VocabularyStENikEntity")
    fun deleteVocabularyStENikEntity()
}