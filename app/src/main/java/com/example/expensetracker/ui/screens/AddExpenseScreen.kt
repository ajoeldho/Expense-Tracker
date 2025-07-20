package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.ui.viewmodel.AddExpenseUiState
import com.example.expensetracker.ui.viewmodel.AddExpenseViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun AddExpenseScreen(
    navController: NavController,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val categories = viewModel.categories.collectAsState().value

    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
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
                        if (cat != null) {
                            viewModel.addExpense(
                                amount = amt,
                                category = cat,
                                note = note.takeIf { it.isNotBlank() },
                                dateTime = LocalDateTime.of(date, time)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedCategory != null && amount.isNotBlank()
                ) {
                    Text("Save Expense")
                }
                if (uiState is AddExpenseUiState.Error) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
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