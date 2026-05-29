package best.mobile.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SortedTextBy(
    val sortedBy: String
) : Parcelable {
    BY_LEVEL("level"),
    BY_ID("id"),
    BY_SMART("smart"),
    EMPTY("");

    fun stringToSortedTextBy(sortedBy: String): SortedTextBy {
        return when (sortedBy) {
            "level" -> BY_LEVEL
            "id" -> BY_ID
            "smart" -> BY_SMART
            "BY_LEVEL" -> BY_LEVEL
            "BY_ID" -> BY_ID
            "BY_SMART" -> BY_SMART
            else -> EMPTY
        }
    }
}
