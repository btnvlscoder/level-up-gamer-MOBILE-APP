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
 * el "smart composable" para la pantalla de registro.
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [RegisterViewModel].
 * 2. observar el [RegisterUiState] (el estado de carga/error).
 * 3. gestionar el estado local de los campos de texto (ej. email, pass).
 * 4. manejar los "efectos secundarios" (side-effects) como
 * mostrar un [Snackbar] y navegar al exito.
 * 5. pasar el estado y los eventos al composable "tonto" [RegisterContent].
 *
 * @param onRegisterSuccess funcion lambda para navegar a [HomeScreen].
 * @param onBackClick funcion lambda para manejar la navegacion hacia atras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    // 1. observa el stateflow del viewmodel.
    val uiState by viewModel.uiState.collectAsState()

    // 2. se necesita un 'snackbarhoststate' y un 'coroutinescope'
    //    para poder mostrar mensajes (ej. "registrado").
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 3. launchedeffect se usa para manejar la navegacion y el snackbar.
    //    se dispara solo cuando 'uistate.registersuccess' cambia a 'true'.
    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            scope.launch {
                // (a) primero, muestra el snackbar y *espera* a que termine.
                snackbarHostState.showSnackbar(
                    message = "¡registrado correctamente!",
                    duration = SnackbarDuration.Short
                )
                // (b) segundo, despues de que el snackbar se oculta, navega.
                onRegisterSuccess()
            }
        }
    }

    // 4. el scaffold se define aqui (en el "smart composable")
    //    para que 'snackbarhost' este en el mismo nivel que 'launchedeffect'.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("crear cuenta") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        // 5. estado local para los campos del formulario.
        //    la vista (view) es "duena" de este estado y se lo pasa
        //    al viewmodel solo al momento de hacer clic.
        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var confirmPass by remember { mutableStateOf("") }
        var rut by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var apellidoP by remember { mutableStateOf("") }
        var apellidoM by remember { mutableStateOf("") }

        // 6. delega el dibujo de la ui al composable "tonto".
        RegisterContent(
            paddingValues = paddingValues,
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
            // al hacer clic, se juntan todos los estados locales
            // y se envian al viewmodel para su validacion y procesamiento.
            onRegisterClick = {
                viewModel.doRegister(
                    email, pass, confirmPass, rut, nombre, apellidoP, apellidoM
                )
            }
        )
    }
}

/**
 * el "dumb composable" (tonto) que solo dibuja la ui del registro.
 *
 * no tiene logica de negocio. solo recibe el estado ([RegisterUiState])
 * y los valores/lambdas de los campos de texto.
 *
 * @param paddingValues padding proveido por el [Scaffold] principal.
 * @param uiState el estado actual (para 'isloading' y 'error').
 * @param ... (todos los parametros de los campos de texto).
 * @param onRegisterClick lambda para el boton de "crear cuenta".
 */
@Composable
fun RegisterContent(
    paddingValues: PaddingValues,
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
    // 'lazycolumn' se usa para que el formulario sea 'scrollable'
    // en pantallas pequenas.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // aplica el padding del scaffold
            .padding(16.dp), // padding interno adicional
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- campos del formulario ---
        item { OutlinedTextField(value = rut, onValueChange = onRutChange, label = { Text("rut (sin puntos ni guion)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = nombre, onValueChange = onNombreChange, label = { Text("nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = apellidoP, onValueChange = onApellidoPChange, label = { Text("apellido paterno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = apellidoM, onValueChange = onApellidoMChange, label = { Text("apellido materno") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = email, onValueChange = onEmailChange, label = { Text("email") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = uiState.error != null) }
        item { OutlinedTextField(value = pass, onValueChange = onPassChange, label = { Text("contrasena") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }
        item { OutlinedTextField(value = confirmPass, onValueChange = onConfirmPassChange, label = { Text("confirmar contrasena") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), isError = uiState.error != null) }

        // --- boton de registro o indicador de carga ---
        item {
            Spacer(Modifier.height(16.dp))
            // muestra un 'circularprogressindicator' si 'isloading' es true.
            // de lo contrario, muestra el boton.
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

        // --- mensaje de error ---
        // muestra el texto de error solo si no es nulo.
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