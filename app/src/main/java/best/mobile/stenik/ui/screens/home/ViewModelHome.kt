package best.mobile.stenik.ui.screens.home

import androidx.lifecycle.ViewModel
import best.mobile.domain.UseCaseRepository

class ViewModelHome(
    private val useCaseRepository: UseCaseRepository
): ViewModel() {

    fun getLaunchListen() = useCaseRepository.getSettings().launchListen

}