package com.example.nutripekes_android

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartDayViewModel : ViewModel() {
    private val TIME_LIMIT = 50


    private val _elapsedTime = MutableStateFlow(0)

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup

    private var timerJob : Job? = null

    fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = viewModelScope.launch {
            while ( _elapsedTime.value < TIME_LIMIT) {
                delay(1000)
                _elapsedTime.value++
            }

            _showPopup.value = true
        }
    }

    fun PauseTimer() {
        timerJob?.cancel()
    }

    fun dismissPopup() {
        _elapsedTime.value = 0
        _showPopup.value = false
        PauseTimer()
    }
}