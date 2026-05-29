package best.mobile.data.entity


import androidx.room.Entity
import best.mobile.entities.TextStENikInterface


@Entity(tableName = "TextStENikEntity", primaryKeys = ["id","language"])
data class TextStENikEntity(
    override var id: Int,
    var language: String,
    override var vocabulary: String,
    override var text: String,
    override var level: Int,
    var idInit: String,
): TextStENikInterface