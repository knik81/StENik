package best.mobile.data.di

import android.content.Context
import android.content.SharedPreferences
import best.mobile.data.firebase.Authentification
import best.mobile.data.firebase.FireStoreDatabase
import best.mobile.data.repositoty.RepositoryMain
import best.mobile.data.repositoty.RepositoryROOM
import best.mobile.data.repositoty.RepositorySharedPreference
import best.mobile.data.repositoty.file_excel.ReadExcelFile
import best.mobile.data.repositoty.room.provideStENikDatabase
import best.mobile.data.tts.MainTTS
import best.mobile.data.tts.SpeechRecognizerStENik
import best.mobile.data.tts.TTStENik
import best.mobile.entities.SharedPrefConst.PREFS_NAME
import best.mobile.entities.SpeechRecognizerStENikInterface
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val dataModuleKoin = module {


    singleOf(::RepositoryMain)
    singleOf(::RepositorySharedPreference)

    singleOf(::provideStENikDatabase)
    singleOf(::RepositoryROOM)

    singleOf(::Authentification)
    singleOf(::FireStoreDatabase)

    singleOf(::MainTTS)
    factoryOf(::TTStENik)


    singleOf(::ReadExcelFile)

    single<SpeechRecognizerStENikInterface> {
        SpeechRecognizerStENik(
            context = androidContext()
        )
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }
}


