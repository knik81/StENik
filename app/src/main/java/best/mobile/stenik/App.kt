package best.mobile.stenik

import android.app.Application
import android.util.Log
import best.mobile.data.di.dataModuleKoin
import best.mobile.domain.di.domainModuleKoin
import best.mobile.entities.Utils.TAG_STENIK
import best.mobile.stenik.di.mainModuleKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(mainModuleKoin,dataModuleKoin,domainModuleKoin)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG_STENIK,"onTerminate")
    }

}