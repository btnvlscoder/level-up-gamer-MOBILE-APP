package com.example.levelupgamermobile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.R
import com.example.levelupgamermobile.controller.LoginViewModel
import com.example.levelupgamermobile.controller.LoginUiState // import explicito

/**
 * el "smart composable" para la pantalla de login.
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [LoginViewModel].
 * 2. observar el [LoginUiState] (el estado).
 * 3. manejar la logica de navegacion (efectos secundarios)
 * basada en los cambios de estado (ej. [LoginUiState.loginSuccess]).
 * 4. pasar el estado y los eventos al composable "tonto" [LoginContent].
 *
 * @param onLoginSuccess funcion lambda para navegar a [HomeScreen].
 * @param onRegisterClick funcion lambda para navegar a [RegisterScreen].
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // 1. observa el stateflow del viewmodel.
    //    'uistate' se actualizara automaticamente cada vez que
    //    el estado en el viewmodel cambie.
    val uiState by viewModel.uiState.collectAsState()

    // 2. launchedeffect se usa para manejar "efectos secundarios"
    //    (side-effects) de forma segura. en este caso, se dispara
    //    para navegar solo cuando 'loginsuccess' cambia a 'true'.
    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    // 3. delega la logica de la ui al composable "tonto".
    LoginContent(
        uiState = uiState,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onLoginClick = { viewModel.doLogin() },
        onRegisterClick = onRegisterClick
    )
}

/**
 * el "dumb composable" (tonto) que dibuja la ui.
 *
 * no tiene logica de negocio. solo recibe el estado ([LoginUiState])
 * y las funciones (lambdas) que debe ejecutar ante eventos (clics,
 * cambios de texto).
 */
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // usamos un 'box' como contenedor raiz para poder
    // superponer el 'surface' de carga (overlay) encima
    // del 'scaffold' cuando 'isloading' es true.
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
                // 1. logo de la app
                Image(
                    // r.drawable.logo debe existir en 'res/drawable'
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "level-up gamer logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 32.dp)
                )

                Text(
                    "iniciar sesion",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(32.dp))

                // 2. campos de texto
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    label = { Text("email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error != null // se marca en rojo si hay error
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = onPasswordChange,
                    label = { Text("contrasena") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.error != null,
                    visualTransformation = PasswordVisualTransformation() // oculta el texto
                )
                Spacer(Modifier.height(24.dp))

                // 3. boton de ingreso
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    // el boton se deshabilita si esta cargando
                    // o si los campos estan vacios.
                    enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.pass.isNotBlank()
                ) {
                    Text("INGRESAR")
                }

                // 4. seccion de error
                if (uiState.error != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // 5. boton de registro
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onRegisterClick, enabled = !uiState.isLoading) {
                    Text("¿no tienes cuenta? registrate")
                }
            }
        }

        // 6. overlay de carga
        // si 'isloading' es true, dibuja un 'surface' semi-transparente
        // encima de todo, bloqueando la ui y mostrando un indicador.
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