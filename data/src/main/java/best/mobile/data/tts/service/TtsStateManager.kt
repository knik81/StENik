package best.mobile.data.tts.service

import best.mobile.data.tts.TTStENik
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TtsStateManager {
    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _tTStENik: MutableStateFlow<TTStENik?> = MutableStateFlow(null)
    val tTStENik: Flow<TTStENik?> = _tTStENik.asStateFlow()

    fun setTTStENik(setTTStENik: TTStENik) {
        _tTStENik.value = setTTStENik
    }

    fun pressButtonPaused() {
        _isPaused.value = !_isPaused.value
    }
}
