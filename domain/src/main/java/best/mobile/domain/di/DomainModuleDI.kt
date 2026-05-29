package best.mobile.domain.di


import best.mobile.domain.UseCaseAuthentification
import best.mobile.domain.UseCaseRepository
import best.mobile.domain.UseCaseSpeechTTS
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val domainModuleKoin = module {
    singleOf(::UseCaseAuthentification)
    singleOf(::UseCaseRepository)
    singleOf(::UseCaseSpeechTTS)
}