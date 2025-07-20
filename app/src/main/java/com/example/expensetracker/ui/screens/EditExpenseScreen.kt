package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.ui.viewmodel.AddExpenseUiState
import com.example.expensetracker.ui.viewmodel.AddExpenseViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import com.example.expensetracker.ui.components.ErrorState

@Composable
fun EditExpenseScreen(
    navController: NavController,
    expenseId: String?,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val categories = viewModel.categories.collectAsState().value
    // For demo, we assume expense is loaded externally. In production, fetch by ID.
    val expense = remember { mutableStateOf<Expense?>(null) }

    var amount by remember { mutableStateOf(expense.value?.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf<Category?>(expense.value?.category) }
    var note by remember { mutableStateOf(expense.value?.note ?: "") }
    var date by remember { mutableStateOf(expense.value?.dateTime?.toLocalDate() ?: LocalDate.now()) }
    var time by remember { mutableStateOf(expense.value?.dateTime?.toLocalTime() ?: LocalTime.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Expense") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.length <= 10) amount = it },
                    label = { Text("Amount *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Amount Input" }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CategoryDropdown(categories, selectedCategory) { selectedCategory = it }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Note Input" }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = date.toString(),
                        onValueChange = {},
                        label = { Text("Date") },
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = time.toString().substring(0,5),
                        onValueChange = {},
                        label = { Text("Time") },
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: 0.0
                        val cat = selectedCategory ?: categories.firstOrNull()
                        if (cat != null && expense.value != null) {
                            viewModel.updateExpense(
                                expense.value!!.copy(
                                    amount = amt,
                                    category = cat,
                                    note = note.takeIf { it.isNotBlank() },
                                    dateTime = LocalDateTime.of(date, time)
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedCategory != null && amount.isNotBlank()
                ) {
                    Text("Update Expense")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // TODO: Implement delete logic
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Expense")
                }
                if (uiState is AddExpenseUiState.Error) {
                    ErrorState(
                        message = uiState.message,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (uiState is AddExpenseUiState.Success) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory?.let { it.emoji + " " + it.name } ?: "Select Category *",
            onValueChange = {},
            readOnly = true,
            label = { Text("Category *") },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.emoji + " " + category.name) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
} 