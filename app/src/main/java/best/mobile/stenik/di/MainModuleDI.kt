package best.mobile.stenik.di

import best.mobile.stenik.ViewModelMainActivity
import best.mobile.stenik.ui.screens.authentification.ViewModelAuthentification
import best.mobile.stenik.ui.screens.home.ViewModelHome
import best.mobile.stenik.ui.screens.listen.ViewModelListen
import best.mobile.stenik.ui.screens.settings.ViewModelSettings
import best.mobile.stenik.ui.screens.test.ViewModelTest
import best.mobile.stenik.ui.screens.vocabulary.ViewModelVocabularyMain
import best.mobile.stenik.ui.screens.vocabulary.ViewModelVocabularyTextEdit
import best.mobile.stenik.ui.screens.vocabulary.ViewModelVocabularyNameCustom
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModuleKoin = module {
    viewModelOf(::ViewModelHome)
    viewModelOf(::ViewModelAuthentification)
    viewModelOf(::ViewModelSettings)
    viewModelOf(::ViewModelListen)
    viewModelOf(::ViewModelVocabularyMain)
    viewModelOf(::ViewModelVocabularyTextEdit)
    viewModelOf(::ViewModelVocabularyNameCustom)
    viewModelOf(::ViewModelTest)
    viewModelOf(::ViewModelMainActivity)

}
