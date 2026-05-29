package best.mobile.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Locale

@Parcelize
@Serializable
enum class LanguageStENik(
    val locale: Locale,
    val description: String,
    val descriptionShort: String
) : Parcelable {
    EMPTY(Locale.US, "Пустышка","--"),
    RUSSIAN(Locale.Builder().setLanguage("ru").setRegion("ru").build(), "Russian", "RU"),
    US(Locale.US, "United State", "US"),
    UK(Locale.UK, "United Kingdom", "UK"),
  //  ENGLISH(Locale.ENGLISH, "English","EN"),
    FRANCE(Locale.FRANCE, "France", "FR"),
    ITALIAN(Locale.ITALIAN, "Italian", "IT"),
    GERMAN(Locale.GERMAN, "Germany", "DE");


    //Мэппинг LanguageStENik со String. Это сделано для БД. БД может хранить только String
    private fun getMappingStringByLanguageStENik() = mapOf(
        "RUSSIAN" to RUSSIAN,
        "US" to US,
        "UK" to UK,
        "FRANCE" to FRANCE,
        "ITALIAN" to ITALIAN,
        "GERMAN" to GERMAN,
        "EMPTY" to EMPTY,
        "--" to EMPTY
    )


    //Этот метод нужен в репозитории. Из БД получаю String, а программе нужен LanguageStENik
    fun stringToLanguageStENik(string: String) =
        getMappingStringByLanguageStENik().getValue(string)



    //Этот метод нужен в репозитории. Программа работает с LanguageStENik, а БД сохраняет в String
    fun languageStENikToString(languageStENik: LanguageStENik): String {
        val stringList =
            (getMappingStringByLanguageStENik().filter { it.value == languageStENik }).keys.toList()
        return if (stringList.isNotEmpty()) stringList.first()
        else "Ошибка"
    }
}

