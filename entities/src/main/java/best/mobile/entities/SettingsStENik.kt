package best.mobile.entities

data class SettingsStENik(
    var languageMain: LanguageStENik = LanguageStENik.RUSSIAN,
    var languageFirst: LanguageStENik = LanguageStENik.US,
    var languageSecond: LanguageStENik = LanguageStENik.EMPTY,
    var launchListen: Boolean = false,
    var readingInTest: Boolean = false,
    var sortedTextBy: SortedTextBy = SortedTextBy.BY_ID,
    var amountVariantsForTest: Int = 5,
    var repeatListenAmount: Int = 1,
    var repeatEverything: Boolean = false,
    var autoSaveToFirebase: Boolean = false,
    var autoLoadFromFirebase: Boolean = false,
    var readSpeedMainLanguage: Float = 1f,
    var readSpeedFirstLanguage: Float = 1f,
    var readSpeedSecondLanguage: Float = 1f,
    var textSize: Int = 14,
    var sigma: Int = 34,
    var bCof: Float = 1f,
    // var vocabularyMain: String = "Test",
    var timeMillsSecForSpeech: Int = 4,
    var playBackground: Boolean = false,
    var vocabularyTextDefault: String = "Test",
    var textOnlyDefaultVocabulary: Boolean = true,
    var delayReadText: Int = 0,
    ) {

    fun getLanguageList(): ArrayList<LanguageStENik> {
        return if (languageSecond != LanguageStENik.EMPTY)
            arrayListOf(
                languageMain, languageFirst, languageSecond
            )
        else
            arrayListOf(
                languageMain, languageFirst
            )
    }

    fun getNewInstance(): SettingsStENik {
        return SettingsStENik(
            languageMain = this.languageMain,
            languageFirst = this.languageFirst,
            languageSecond = this.languageSecond,
            launchListen = this.launchListen,
            readingInTest = this.readingInTest,
            sortedTextBy = this.sortedTextBy,
            amountVariantsForTest = this.amountVariantsForTest,
            repeatListenAmount = this.repeatListenAmount,
            repeatEverything = this.repeatEverything,
            autoSaveToFirebase = this.autoSaveToFirebase,
            autoLoadFromFirebase = this.autoLoadFromFirebase,
            readSpeedMainLanguage = this.readSpeedMainLanguage,
            readSpeedFirstLanguage = this.readSpeedFirstLanguage,
            readSpeedSecondLanguage = this.readSpeedSecondLanguage,
            textSize = this.textSize,
            sigma = this.sigma,
            bCof = this.bCof,
            // vocabularyMain = this.vocabularyMain,
            timeMillsSecForSpeech = this.timeMillsSecForSpeech,
            playBackground = this.playBackground,
            vocabularyTextDefault = this.vocabularyTextDefault,
            textOnlyDefaultVocabulary = this.textOnlyDefaultVocabulary,
            delayReadText = this.delayReadText
        )
    }

}
