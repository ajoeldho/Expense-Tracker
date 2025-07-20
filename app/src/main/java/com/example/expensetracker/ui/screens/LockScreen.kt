package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.ui.viewmodel.AuthUiState
import com.example.expensetracker.ui.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.ui.components.ErrorState

@Composable
fun LockScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navController.navigate("home") {
                popUpTo("lock") { inclusive = true }
            }
        } else if (uiState is AuthUiState.Error) {
            error = uiState.message
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Lock, contentDescription = "Lock Icon", modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Enter PIN",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { contentDescription = "PIN Entry Title" }
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 4) pin = it },
                label = { Text("PIN") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .semantics { contentDescription = "PIN Input Field" }
                    .fillMaxWidth(0.5f),
                isError = error != null
            )
            if (error != null) {
                ErrorState(
                    message = error!!,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.authenticate(pin)
                },
                modifier = Modifier.semantics { contentDescription = "Unlock Button" },
                enabled = pin.length == 4
            ) {
                Text("Unlock")
            }
        }
    }
} 