package best.mobile.entities

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
@Entity(tableName = "TextStENik")
data class TextStENik(
    @ColumnInfo(name = "id")
    override var id: Int,

    @ColumnInfo(name = "language")
    var language: LanguageStENik,

    @ColumnInfo(name = "vocabulary")
    override var vocabulary: String,

    @ColumnInfo(name = "text")
    override var text: String,

    @ColumnInfo(name = "level")
    override var level: Int,

    @ColumnInfo(name = "idInit")
    var idInit: String

) : TextStENikInterface, Parcelable {
    @IgnoredOnParcel
    var textMutableState: String by mutableStateOf("")
}

