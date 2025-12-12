package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.state.RegisterUiState
import com.example.levelupgamermobile.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla "inteligente" de Registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "¡Registrado correctamente!",
                    duration = SnackbarDuration.Short
                )
                onRegisterSuccess()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        RegisterContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onRegisterClick = { email, pass, confirmPass, rut, nombre, apellidoP, apellidoM ->
                viewModel.doRegister(email, pass, confirmPass, rut, nombre, apellidoP, apellidoM)
            }
        )
    }
}

@Composable
fun RegisterContent(
    paddingValues: PaddingValues,
    uiState: RegisterUiState,
    onRegisterClick: (String, String, String, String, String, String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoP by remember { mutableStateOf("") }
    var apellidoM by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT (12.345.678-9)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = apellidoP,
                onValueChange = { apellidoP = it },
                label = { Text("Apellido Paterno") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = apellidoM,
                onValueChange = { apellidoM = it },
                label = { Text("Apellido Materno") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onRegisterClick(email, pass, confirmPass, rut, nombre, apellidoP, apellidoM)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("REGISTRARSE")
                }
            }

            if (uiState.error != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
