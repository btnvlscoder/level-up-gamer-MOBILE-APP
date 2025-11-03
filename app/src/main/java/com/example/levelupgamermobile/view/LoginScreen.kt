package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.LoginViewModel

/**
 * Pantalla "inteligente" de Login.
 * Esta pantalla SÍ conoce al ViewModel.
 *
 * @param onLoginSuccess Esta es una "lambda" (función) que
 * le pasaremos desde MainActivity para decirle qué hacer
 * (navegar) cuando el login sea exitoso.
 *
 * @param onRegisterClick Lambda para navegar a la pantalla
 * de registro.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // (1) Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // (2) LaunchedEffect
    // Es un "oyente" que se dispara cuando una variable cambia.
    // Aquí, se dispara si "loginSuccess" cambia a "true".
    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            // ¡Navega!
            onLoginSuccess()
        }
    }

    // (3) Llamamos a la pantalla "tonta" (Content)
    // Le pasamos el estado y las funciones del ViewModel.
    LoginContent(
        uiState = uiState,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onLoginClick = { viewModel.doLogin() },
        onRegisterClick = onRegisterClick
    )
}

/**
 * Pantalla "tonta" (Dumb Composable) de Login.
 * Esta pantalla NO conoce al ViewModel. Solo recibe
 * datos y "avisa" cuando hay clics.
 */
@Composable
fun LoginContent(
    uiState: com.example.levelupgamermobile.controller.LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(32.dp))

            // Campo de Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.error != null // Marca en rojo si hay error
            )
            Spacer(Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = uiState.pass,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.error != null, // Marca en rojo si hay error
                visualTransformation = PasswordVisualTransformation() // Oculta la contraseña
            )
            Spacer(Modifier.height(24.dp))

            // (4) Botón de Login o Indicador de Carga
            // Si está cargando (isLoading), muestra la ruedita.
            // Si no, muestra el botón.
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.email.isNotBlank() && uiState.pass.isNotBlank()
                ) {
                    Text("INGRESAR")
                }
            }

            // (5) Mensaje de Error
            // Muestra el error del backend si existe.
            if (uiState.error != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // (6) Botón de Registro
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onRegisterClick) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}