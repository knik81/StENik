package best.mobile.data.entity

import androidx.room.Entity
import best.mobile.entities.VocabularyStENikInterface

@Entity(tableName = "VocabularyStENikEntity", primaryKeys = ["name"])
data class VocabularyStENikEntity(
    override val name: String,
    override val isSelected: Boolean
): VocabularyStENikInterface
