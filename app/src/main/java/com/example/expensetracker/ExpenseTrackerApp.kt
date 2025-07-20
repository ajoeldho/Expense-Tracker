package com.example.expensetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Expense Tracker, required for Hilt DI.
 */
@HiltAndroidApp
class ExpenseTrackerApp : Application() 