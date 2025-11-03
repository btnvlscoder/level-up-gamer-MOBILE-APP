package com.example.levelupgamermobile.view

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.RegisterUiState
import com.example.levelupgamermobile.controller.RegisterViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla "inteligente" de Registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // (1) Necesitamos un "SnackbarHostState" para mostrar el mensaje
    val snackbarHostState = remember { SnackbarHostState() }
    // (2) Necesitamos un "CoroutineScope" para lanzar el snackbar
    val scope = rememberCoroutineScope()

    // (3) LaunchedEffect para el éxito del registro
    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {

            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
            scope.launch {
                // (A) Primero, muestra el mensaje Y ESPERA
                snackbarHostState.showSnackbar(
                    message = "¡Registrado correctamente!",
                    duration = SnackbarDuration.Short
                )
                // (B) DESPUÉS de que el snackbar termine, navega
                onRegisterSuccess()
            }
            // --- FIN DE LA CORRECCIÓN ---
        }
    }

    // (4) Movemos el Scaffold aquí, a la pantalla "inteligente"
    // para que pueda controlar el SnackbarHost.
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

        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var confirmPass by remember { mutableStateOf("") }
        var rut by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var apellidoP by remember { mutableStateOf("") }
        var apellidoM by remember { mutableStateOf("") }

        RegisterContent(
            paddingValues = paddingValues, // Le pasamos el padding del Scaffold
            uiState = uiState,
            email = email,
            onEmailChange = { email = it },
            pass = pass,
            onPassChange = { pass = it },
            confirmPass = confirmPass,
            onConfirmPassChange = { confirmPass = it },
            rut = rut,
            onRutChange = { rut = it },
            nombre = nombre,
            onNombreChange = { nombre = it },
            apellidoP = apellidoP,
            onApellidoPChange = { apellidoP = it },
            apellidoM = apellidoM,
            onApellidoMChange = { apellidoM = it },
            onRegisterClick = {
                viewModel.doRegister(
                    email, pass, confirmPass, rut, nombre, apellidoP, apellidoM
                )
            }
        )
    }
}

/**
 * Pantalla "tonta" (Dumb Composable) de Registro.
 */
@Composable
fun RegisterContent(
    paddingValues: PaddingValues, // ¡Recibe el padding!
    uiState: RegisterUiState,
    email: String, onEmailChange: (String) -> Unit,
    pass: String, onPassChange: (String) -> Unit,
    confirmPass: String, onConfirmPassChange: (String) -> Unit,
    rut: String, onRutChange: (String) -> Unit,
    nombre: String, onNombreChange: (String) -> Unit,
    apellidoP: String, onApellidoPChange: (String) -> Unit,
    apellidoM: String, onApellidoMChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // ¡Aplica el padding!
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Campos del Formulario (Sin cambios) ---
        item { OutlinedTextField(value = rut, onValueChange = onRutChange, label = { Text("RUT (sin puntos ni guion)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = nombre, onValueChange = onNombreChange, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = apellidoP, onValueChange = onApellidoPChange, label = { Text("Apellido Paterno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = apellidoM, onValueChange = onApellidoMChange, label = { Text("Apellido Materno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = email, onValueChange = onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = pass, onValueChange = onPassChange, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }
        item { OutlinedTextField(value = confirmPass, onValueChange = onConfirmPassChange, label = { Text("Confirmar Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }

        // --- Botón de Registro o Carga (Sin cambios) ---
        item {
            Spacer(Modifier.height(16.dp))
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CREAR CUENTA")
                }
            }
        }

        // --- Mensaje de Error (Sin cambios) ---
        if (uiState.error != null) {
            item {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}