package com.example.levelupgamermobile.view

// ... (imports de layout)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.* // Importa todo Material 3
import androidx.compose.runtime.* // Importa remember, etc.
// ... (imports de UI y ViewModel)
import com.example.levelupgamermobile.controller.RegisterViewModel
import com.example.levelupgamermobile.controller.RegisterUiState
import kotlinx.coroutines.launch // ¡Importa launch!

/**
 * Pantalla "inteligente" de Registro.
 */
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // (1) ¡NUEVO! Necesitamos un "SnackbarHostState" para mostrar el mensaje
    val snackbarHostState = remember { SnackbarHostState() }
    // (2) ¡NUEVO! Necesitamos un "CoroutineScope" para lanzar el snackbar
    val scope = rememberCoroutineScope()

    // (3) LaunchedEffect para el éxito del registro
    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            // ¡Mostramos el mensaje!
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "¡Registrado correctamente!",
                    duration = SnackbarDuration.Short
                )
            }
            // Navegamos
            onRegisterSuccess()
        }
    }

    // (4) ¡NUEVO! Movemos el Scaffold aquí, a la pantalla "inteligente"
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

        // --- El resto del código es casi igual ---

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
            // onBackClick ya no se necesita aquí, lo maneja el TopAppBar
        )
    }
}

/**
 * Pantalla "tonta" (Dumb Composable) de Registro.
 * (5) ¡CAMBIO! Esta ya no necesita un Scaffold.
 * Solo es el formulario (el LazyColumn).
 */
@Composable
fun RegisterContent(
    paddingValues: androidx.compose.foundation.layout.PaddingValues, // ¡Recibe el padding!
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