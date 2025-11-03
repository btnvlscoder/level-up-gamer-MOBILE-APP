package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.RegisterViewModel
import com.example.levelupgamermobile.controller.RegisterUiState // ¡Importa el UiState correcto!

/**
 * Pantalla "inteligente" de Registro.
 */
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    // (1) Observa el RegisterUiState
    val uiState by viewModel.uiState.collectAsState()

    // (2) Reacciona al éxito del registro
    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            onRegisterSuccess()
        }
    }

    // (3) Estado local para los campos (propiedad de la Vista)
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoP by remember { mutableStateOf("") }
    var apellidoM by remember { mutableStateOf("") }

    // (4) Llama a la UI "tonta"
    RegisterContent(
        uiState = uiState, // Le pasa el estado del ViewModel
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
            // (5) Llama al ViewModel con los datos de la Vista
            viewModel.doRegister(
                email, pass, confirmPass, rut, nombre, apellidoP, apellidoM
            )
        },
        onBackClick = onBackClick
    )
}

/**
 * Pantalla "tonta" (Dumb Composable) de Registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    uiState: RegisterUiState, // (6) Espera un RegisterUiState
    email: String, onEmailChange: (String) -> Unit,
    pass: String, onPassChange: (String) -> Unit,
    confirmPass: String, onConfirmPassChange: (String) -> Unit,
    rut: String, onRutChange: (String) -> Unit,
    nombre: String, onNombreChange: (String) -> Unit,
    apellidoP: String, onApellidoPChange: (String) -> Unit,
    apellidoM: String, onApellidoMChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Campos del Formulario ---
            item {
                OutlinedTextField(
                    value = rut,
                    onValueChange = onRutChange,
                    label = { Text("RUT (sin puntos ni guion)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error != null // (7) Lee el error del UiState
                )
            }
            // (Todos los demás OutlinedTextField...)
            item { OutlinedTextField(value = nombre, onValueChange = onNombreChange, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
            item { OutlinedTextField(value = apellidoP, onValueChange = onApellidoPChange, label = { Text("Apellido Paterno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
            item { OutlinedTextField(value = apellidoM, onValueChange = onApellidoMChange, label = { Text("Apellido Materno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
            item { OutlinedTextField(value = email, onValueChange = onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
            item { OutlinedTextField(value = pass, onValueChange = onPassChange, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }
            item { OutlinedTextField(value = confirmPass, onValueChange = onConfirmPassChange, label = { Text("Confirmar Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }

            // --- Botón de Registro o Carga ---
            item {
                Spacer(Modifier.height(16.dp))
                if (uiState.isLoading) { // (8) Lee isLoading del UiState
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

            // --- Mensaje de Error ---
            if (uiState.error != null) { // (9) Lee el error del UiState
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
}