package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.ui.viewmodel.SettingsUiState
import com.example.expensetracker.ui.viewmodel.SettingsViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    var pinInput by remember { mutableStateOf("") }
    var showPinDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Theme", modifier = Modifier.weight(1f))
                    Switch(
                        checked = viewModel.themeViewModel.isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme() }
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set/Change PIN", modifier = Modifier.weight(1f))
                    Button(onClick = { showPinDialog = true }) {
                        Text("Set PIN")
                    }
                }
                if (showPinDialog) {
                    AlertDialog(
                        onDismissRequest = { showPinDialog = false },
                        title = { Text("Set PIN") },
                        text = {
                            OutlinedTextField(
                                value = pinInput,
                                onValueChange = { if (it.length <= 4) pinInput = it },
                                label = { Text("Enter 4-digit PIN") },
                                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword)
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (pinInput.length == 4) {
                                        viewModel.setPin(pinInput) // In production, hash the PIN
                                        showPinDialog = false
                                        pinInput = ""
                                    }
                                },
                                enabled = pinInput.length == 4
                            ) { Text("Save") }
                        },
                        dismissButton = {
                            Button(onClick = { showPinDialog = false }) { Text("Cancel") }
                        }
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Remove PIN", modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.clearPin() }) {
                        Text("Remove")
                    }
                }
                if (uiState is SettingsUiState.Error) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (uiState is SettingsUiState.Success) {
                    Text(
                        text = "Settings updated!",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
} 