package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing theme switching (light/dark) with persistence.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    // TODO: Integrate DataStore for persistence
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        // TODO: Save to DataStore
    }
} 