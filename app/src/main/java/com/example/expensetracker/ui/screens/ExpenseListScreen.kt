package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.ui.viewmodel.ExpenseListUiState
import com.example.expensetracker.ui.viewmodel.ExpenseListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    var filter by remember { mutableStateOf(ExpenseFilter.TODAY) }

    LaunchedEffect(filter) {
        val now = LocalDateTime.now()
        when (filter) {
            ExpenseFilter.TODAY -> viewModel.loadExpensesBetween(
                now.toLocalDate().atStartOfDay(),
                now
            )
            ExpenseFilter.WEEK -> viewModel.loadExpensesBetween(
                now.toLocalDate().with(java.time.DayOfWeek.MONDAY).atStartOfDay(),
                now
            )
            ExpenseFilter.MONTH -> viewModel.loadExpensesBetween(
                now.toLocalDate().withDayOfMonth(1).atStartOfDay(),
                now
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Expenses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterButton("Today", filter == ExpenseFilter.TODAY) { filter = ExpenseFilter.TODAY }
                    FilterButton("Week", filter == ExpenseFilter.WEEK) { filter = ExpenseFilter.WEEK }
                    FilterButton("Month", filter == ExpenseFilter.MONTH) { filter = ExpenseFilter.MONTH }
                }
                Spacer(modifier = Modifier.height(16.dp))
                when (uiState) {
                    is ExpenseListUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is ExpenseListUiState.Error -> {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is ExpenseListUiState.Success -> {
                        if (uiState.expenses.isEmpty()) {
                            Text(
                                text = "No expenses found.",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uiState.expenses) { expense ->
                                    ExpenseListItem(expense)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class ExpenseFilter { TODAY, WEEK, MONTH }

@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = if (selected) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        else ButtonDefaults.buttonColors(),
        modifier = Modifier.weight(1f)
    ) {
        Text(text)
    }
}

@Composable
fun ExpenseListItem(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "-₹${expense.amount}", color = MaterialTheme.colorScheme.error)
                Text(text = expense.category.emoji + " " + expense.category.name)
            }
            if (!expense.note.isNullOrBlank()) {
                Text(text = expense.note ?: "", style = MaterialTheme.typography.bodySmall)
            }
            Text(text = expense.dateTime.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
} 