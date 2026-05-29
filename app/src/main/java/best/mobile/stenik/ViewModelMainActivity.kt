package best.mobile.stenik

import android.app.Application
import android.app.NotificationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import best.mobile.domain.UseCaseRepository
import best.mobile.domain.UseCaseSpeechTTS
import best.mobile.entities.Utils.NOTIFICATION_ID
import com.google.api.Context

class ViewModelMainActivity(
    application: Application,
    val useCaseSpeechTTS: UseCaseSpeechTTS
): AndroidViewModel(application) {

    private val appContext = application.applicationContext

    val notificationManager =
        appContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

    fun stopTTS() {
        useCaseSpeechTTS.stopTTS(context = appContext)
        //useCaseSpeechTTS.stopServiceTTS(context = appContext)
        notificationManager.cancel(NOTIFICATION_ID)
    }
}