package com.example.expensetracker.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.ui.navigation.ExpenseNavHost
import com.example.expensetracker.ui.viewmodel.ThemeViewModel

/**
 * Root composable for the Expense Tracker app.
 */
@Composable
fun ExpenseTrackerApp() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    ExpenseTrackerTheme(darkTheme = themeViewModel.isDarkTheme) {
        ExpenseNavHost()
    }
} 