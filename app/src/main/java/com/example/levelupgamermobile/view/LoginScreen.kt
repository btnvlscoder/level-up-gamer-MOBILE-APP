package com.example.levelupgamermobile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // ¡Importa Color!
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.LoginViewModel

/**
 * Pantalla "inteligente" de Login.
 * (Esta función no cambia)
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

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
 * ¡Aquí es donde ocurre la magia!
 */
@Composable
fun LoginContent(
    uiState: com.example.levelupgamermobile.controller.LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // (1) ¡NUEVO! Usamos un Box para poder superponer elementos
    Box(
        modifier = Modifier.fillMaxSize()
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

                // --- Campos de Texto (Sin cambios) ---
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error != null
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error != null,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(Modifier.height(24.dp))

                // (2) ¡CAMBIO! El botón ya no desaparece
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    // El botón se deshabilita si está cargando O si los campos están vacíos
                    enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.pass.isNotBlank()
                ) {
                    Text("INGRESAR")
                }

                // --- Error y Botón de Registro (Sin cambios) ---
                if (uiState.error != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onRegisterClick, enabled = !uiState.isLoading) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }

        // (3) ¡NUEVO! El Overlay de Carga
        // Si uiState.isLoading es true, esta sección se dibujará
        // ENCIMA del Scaffold.
        if (uiState.isLoading) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                // Un color negro semi-transparente para el fondo
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}