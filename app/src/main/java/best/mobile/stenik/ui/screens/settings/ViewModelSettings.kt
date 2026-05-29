package best.mobile.stenik.ui.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import best.mobile.domain.UseCaseRepository
import best.mobile.entities.SettingsStENik

class ViewModelSettings(
    private val useCaseRepository: UseCaseRepository
) : ViewModel() {

    private val _settingsStENik = mutableStateOf(useCaseRepository.getSettings())
    val settingsStENik: MutableState<SettingsStENik>
        get () = _settingsStENik

    fun putSettings() {
        useCaseRepository.putSettings(settingsStENik.value)
    }

}