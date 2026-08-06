package com.example.nothingwidget.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nothingwidget.data.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isDarkTheme: Boolean = true,
    val isHapticFeedbackEnabled: Boolean = true,
    val is24HourClock: Boolean = true,
    val isCelsius: Boolean = true,
    val widgetUpdateIntervalMinutes: Int = 15,
    val selectedCity: String = "London"
)

class SettingsViewModel(
    private val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            appPreferencesRepository.isDarkThemeFlow.collect { isDark ->
                _state.value = _state.value.copy(isDarkTheme = isDark)
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            appPreferencesRepository.setDarkTheme(!_state.value.isDarkTheme)
        }
    }

    fun toggleHaptics() {
        _state.value = _state.value.copy(isHapticFeedbackEnabled = !_state.value.isHapticFeedbackEnabled)
    }

    fun toggle24h() {
        _state.value = _state.value.copy(is24HourClock = !_state.value.is24HourClock)
    }

    fun toggleCelsius() {
        _state.value = _state.value.copy(isCelsius = !_state.value.isCelsius)
    }

    fun setUpdateInterval(mins: Int) {
        _state.value = _state.value.copy(widgetUpdateIntervalMinutes = mins)
    }

    fun setSelectedCity(city: String) {
        _state.value = _state.value.copy(selectedCity = city)
    }
}
