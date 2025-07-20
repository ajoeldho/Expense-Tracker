package com.example.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.screens.*

sealed class Screen(val route: String) {
    object Lock : Screen("lock")
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")
    object EditExpense : Screen("edit_expense/{expenseId}")
    object ExpenseList : Screen("expense_list")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
    object ThemeSettings : Screen("theme_settings")
    object SecuritySetup : Screen("security_setup")
}

@Composable
fun ExpenseNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Lock.route) {
        composable(Screen.Lock.route) { LockScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.AddExpense.route) { AddExpenseScreen(navController) }
        composable(Screen.EditExpense.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")
            EditExpenseScreen(navController, expenseId)
        }
        composable(Screen.ExpenseList.route) { ExpenseListScreen(navController) }
        composable(Screen.Analytics.route) { AnalyticsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.ThemeSettings.route) { ThemeSettingsScreen(navController) }
        composable(Screen.SecuritySetup.route) { SecuritySetupScreen(navController) }
    }
} 