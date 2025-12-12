package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.R
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.state.LoginUiState
import com.example.levelupgamermobile.ui.viewmodel.LoginViewModel

/**
 * Pantalla "inteligente" de Login.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
 */
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
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
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Level-Up Gamer Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 32.dp)
                )

                Text(
                    "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(32.dp))

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

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.pass.isNotBlank()
                ) {
                    Text("INGRESAR")
                }

                if (uiState.error != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onRegisterClick, enabled = !uiState.isLoading) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }
        }

        if (uiState.isLoading) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
