package best.mobile.data.repositoty

import android.content.SharedPreferences
import best.mobile.entities.LanguageStENik
import best.mobile.entities.SettingsStENik
import best.mobile.entities.SharedPrefConst
import best.mobile.entities.SortedTextBy
import androidx.core.content.edit

class RepositorySharedPreferences(private val sharedPreferences: SharedPreferences) {

    fun getSettings(): SettingsStENik {
        val settingsStENikDefault = SettingsStENik()

        val languageMain = sharedPreferences.getString(
            SharedPrefConst.LANG_MAIN,
            settingsStENikDefault.languageMain.toString()
        ) ?: settingsStENikDefault.languageMain.toString()

        val languageFirst = sharedPreferences.getString(
            SharedPrefConst.LANG_FIRST,
            settingsStENikDefault.languageFirst.toString()
        ) ?: settingsStENikDefault.languageFirst.toString()

        val languageSecond = sharedPreferences.getString(
            SharedPrefConst.LANG_SECOND,
            settingsStENikDefault.languageSecond.toString()
        ) ?: settingsStENikDefault.languageSecond.toString()

        val listenRun = sharedPreferences.getBoolean(
            SharedPrefConst.RUN_LISTEN,
            settingsStENikDefault.launchListen
        )

        val readingInTest = sharedPreferences.getBoolean(
            SharedPrefConst.READ_TEST,
            settingsStENikDefault.readingInTest
        )

        val sortedTextBy = sharedPreferences.getString(
            SharedPrefConst.SORTED_TEXT,
            settingsStENikDefault.sortedTextBy.toString()
        ) ?: settingsStENikDefault.sortedTextBy.toString()

        val amountVariantsForTest = sharedPreferences.getInt(
            SharedPrefConst.TEST_VARIANTS,
            settingsStENikDefault.amountVariantsForTest
        )

        val repeatAmount = sharedPreferences.getInt(
            SharedPrefConst.REPEAT_AMOUNT,
            settingsStENikDefault.repeatListenAmount
        )
        val repeatAll = sharedPreferences.getBoolean(
            SharedPrefConst.REPEAT_ALL,
            settingsStENikDefault.repeatEverything
        )
        val autoSaveToFirebase = sharedPreferences.getBoolean(
            SharedPrefConst.FIREBASE_AUTO_SAVE,
            settingsStENikDefault.autoSaveToFirebase
        )

        val autoLoadFromFirebase = sharedPreferences.getBoolean(
            SharedPrefConst.FIREBASE_AUTO_LOAD,
            settingsStENikDefault.autoLoadFromFirebase
        )

        val readSpeedMainLanguage = sharedPreferences.getFloat(
            SharedPrefConst.READ_SPEED_MAIN,
            settingsStENikDefault.readSpeedMainLanguage
        )

        val readSpeedFirstLanguage = sharedPreferences.getFloat(
            SharedPrefConst.READ_SPEED_FIRST,
            settingsStENikDefault.readSpeedFirstLanguage
        )

        val readSpeedSecondLanguage = sharedPreferences.getFloat(
            SharedPrefConst.READ_SPEED_SECOND,
            settingsStENikDefault.readSpeedSecondLanguage
        )

        val textSize = sharedPreferences.getInt(
            SharedPrefConst.TEXT_SIZE,
            settingsStENikDefault.textSize
        )

        val sigma = sharedPreferences.getInt(
            SharedPrefConst.SIGMA,
            settingsStENikDefault.sigma
        )

        val bCof = sharedPreferences.getFloat(
            SharedPrefConst.B_COF,
            settingsStENikDefault.bCof
        )

        val timeMillsSecForSpeech = sharedPreferences.getInt(
            SharedPrefConst.TIME_MILLS_SEC,
            settingsStENikDefault.timeMillsSecForSpeech
        )

        val inFon = sharedPreferences.getBoolean(
            SharedPrefConst.PLAY_BACKGROUND,
            settingsStENikDefault.playBackground
        )

        val vocabularyTextDefault = sharedPreferences.getString(
            SharedPrefConst.VOCABULARY_DEFAULT,
            settingsStENikDefault.vocabularyTextDefault
        ) ?: settingsStENikDefault.vocabularyTextDefault

        val textOnlyDefaultVocabulary = sharedPreferences.getBoolean(
            SharedPrefConst.TEXT_ONLY_VOCABULARY_DEFAULT,
            settingsStENikDefault.textOnlyDefaultVocabulary
        )

        val delayReadText = sharedPreferences.getInt(
            SharedPrefConst.DELAY_READ_TEXT,
            settingsStENikDefault.delayReadText
        )


        return SettingsStENik(
            languageMain = LanguageStENik.EMPTY.stringToLanguageStENik(languageMain),
            languageFirst = LanguageStENik.EMPTY.stringToLanguageStENik(languageFirst),
            languageSecond = LanguageStENik.EMPTY.stringToLanguageStENik(languageSecond),
            launchListen = listenRun,
            readingInTest = readingInTest,
            sortedTextBy = SortedTextBy.EMPTY.stringToSortedTextBy(sortedTextBy),
            amountVariantsForTest = amountVariantsForTest,
            repeatListenAmount = repeatAmount,
            repeatEverything = repeatAll,
            autoSaveToFirebase = autoSaveToFirebase,
            autoLoadFromFirebase = autoLoadFromFirebase,
            readSpeedMainLanguage = readSpeedMainLanguage,
            readSpeedFirstLanguage = readSpeedFirstLanguage,
            readSpeedSecondLanguage = readSpeedSecondLanguage,
            textSize = textSize,
            sigma = sigma,
            bCof = bCof,
            timeMillsSecForSpeech = timeMillsSecForSpeech,
            playBackground = inFon,
            vocabularyTextDefault = vocabularyTextDefault,
            textOnlyDefaultVocabulary = textOnlyDefaultVocabulary,
            delayReadText = delayReadText
        )
    }


    fun putSettings(settingsStENik: SettingsStENik) {

        sharedPreferences.edit(commit = false) {
            putString(
                SharedPrefConst.LANG_MAIN,
                LanguageStENik.EMPTY.languageStENikToString(settingsStENik.languageMain)
            )

            putString(
                SharedPrefConst.LANG_FIRST,
                LanguageStENik.EMPTY.languageStENikToString(settingsStENik.languageFirst)
            )

            putString(
                SharedPrefConst.LANG_SECOND,
                LanguageStENik.EMPTY.languageStENikToString(settingsStENik.languageSecond)
            )

            putBoolean(
                SharedPrefConst.RUN_LISTEN,
                settingsStENik.launchListen
            )

            putBoolean(
                SharedPrefConst.READ_TEST,
                settingsStENik.readingInTest
            )

            putString(
                SharedPrefConst.SORTED_TEXT,
                settingsStENik.sortedTextBy.sortedBy
            )

            putInt(
                SharedPrefConst.TEST_VARIANTS,
                settingsStENik.amountVariantsForTest
            )

            putInt(
                SharedPrefConst.REPEAT_AMOUNT,
                settingsStENik.repeatListenAmount
            )

            putBoolean(
                SharedPrefConst.REPEAT_ALL,
                settingsStENik.repeatEverything
            )

            putBoolean(
                SharedPrefConst.FIREBASE_AUTO_SAVE,
                settingsStENik.autoSaveToFirebase
            )

            putBoolean(
                SharedPrefConst.FIREBASE_AUTO_LOAD,
                settingsStENik.autoLoadFromFirebase
            )

            putFloat(
                SharedPrefConst.READ_SPEED_MAIN,
                settingsStENik.readSpeedMainLanguage
            )

            putFloat(
                SharedPrefConst.READ_SPEED_FIRST,
                settingsStENik.readSpeedFirstLanguage
            )

            putFloat(
                SharedPrefConst.READ_SPEED_SECOND,
                settingsStENik.readSpeedSecondLanguage
            )

            putInt(
                SharedPrefConst.TEXT_SIZE,
                settingsStENik.textSize
            )
            putInt(
                SharedPrefConst.SIGMA,
                settingsStENik.sigma
            )

            putFloat(
                SharedPrefConst.B_COF,
                settingsStENik.bCof
            )

            putInt(
                SharedPrefConst.TIME_MILLS_SEC,
                settingsStENik.timeMillsSecForSpeech
            )

            putBoolean(
                SharedPrefConst.PLAY_BACKGROUND,
                settingsStENik.playBackground
            )

            putString(
                SharedPrefConst.VOCABULARY_DEFAULT,
                settingsStENik.vocabularyTextDefault
            )

            putBoolean(
                SharedPrefConst.TEXT_ONLY_VOCABULARY_DEFAULT,
                settingsStENik.textOnlyDefaultVocabulary
            )

            putInt(
                SharedPrefConst.DELAY_READ_TEXT,
                settingsStENik.delayReadText
            )
        }
    }
}