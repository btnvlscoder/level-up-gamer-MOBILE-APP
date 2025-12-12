package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.viewmodel.SoporteViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de Soporte (Formulario de Contacto).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteScreen(
    onBackPress: () -> Unit,
    viewModel: SoporteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    // (1) Estado local para los campos
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // (2) Para el Snackbar de "enviado"
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Pre-llenar datos si el usuario está logueado
    LaunchedEffect(uiState.userEmail, uiState.userName) {
        if (uiState.userEmail.isNotEmpty()) {
            correo = uiState.userEmail
        }
        if (uiState.userName.isNotEmpty()) {
            nombre = uiState.userName
        }
    }

    // Manejar éxito o error
    LaunchedEffect(uiState.isSuccess, uiState.errorMessage) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar(
                message = "Ticket enviado con éxito",
                duration = SnackbarDuration.Short
            )
            // Limpiar campos de mensaje
            asunto = ""
            mensaje = ""
            viewModel.resetState()
        }
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = uiState.errorMessage ?: "Error desconocido",
                duration = SnackbarDuration.Short
            )
            viewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Soporte y Contacto") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Contáctanos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary // Verde
                    )
                }
                item {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = uiState.userName.isNotEmpty() // Solo lectura si viene del perfil
                    )
                }
                item {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = uiState.userEmail.isNotEmpty() // Solo lectura si viene del perfil
                    )
                }
                item {
                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it },
                        label = { Text("Mensaje") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5
                    )
                }
                item {
                    Button(
                        onClick = {
                            viewModel.enviarTicket(correo, asunto, mensaje)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading && asunto.isNotBlank() && mensaje.isNotBlank() && correo.isNotBlank()
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("ENVIAR MENSAJE")
                        }
                    }
                }
            }
        }
    }
}
