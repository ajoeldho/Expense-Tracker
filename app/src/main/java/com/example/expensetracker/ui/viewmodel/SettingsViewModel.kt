package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Pin
import com.example.expensetracker.domain.usecase.pin.ClearPinUseCase
import com.example.expensetracker.domain.usecase.pin.GetPinUseCase
import com.example.expensetracker.domain.usecase.pin.SetPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SettingsUiState {
    object Idle : SettingsUiState()
    object Loading : SettingsUiState()
    object Success : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
    data class PinState(val hasPin: Boolean) : SettingsUiState()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setPinUseCase: SetPinUseCase,
    private val getPinUseCase: GetPinUseCase,
    private val clearPinUseCase: ClearPinUseCase,
    val themeViewModel: ThemeViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Idle)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun checkPin() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            val pin = getPinUseCase()
            _uiState.value = SettingsUiState.PinState(pin != null)
        }
    }

    fun setPin(pinHash: String) {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            val result = setPinUseCase(Pin(pinHash))
            _uiState.value = if (result.isSuccess) SettingsUiState.Success else SettingsUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    fun clearPin() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            clearPinUseCase()
            _uiState.value = SettingsUiState.Success
        }
    }

    fun toggleTheme() = themeViewModel.toggleTheme()
} 