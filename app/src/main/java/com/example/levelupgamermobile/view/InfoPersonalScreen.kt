package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.InfoPersonalUiState
import com.example.levelupgamermobile.controller.InfoPersonalViewModel

/**
 * el "smart composable" para la pantalla de informacion personal.
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [InfoPersonalViewModel].
 * 2. observar el [InfoPersonalUiState] (el estado).
 * 3. manejar la logica de navegacion de "logout" (via [LaunchedEffect]).
 * 4. pasar el estado y los eventos al composable "tonto" [InfoPersonalContent].
 *
 * @param onLogoutComplete funcion lambda para navegar de vuelta al [LoginScreen]
 * al completarse el logout.
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras
 * (volver al [ProfileMenuScreen]).
 */
@Composable
fun InfoPersonalScreen(
    viewModel: InfoPersonalViewModel = viewModel(),
    onLogoutComplete: () -> Unit,
    onBackPress: () -> Unit
) {
    // observa el stateflow del viewmodel.
    // 'uistate' se actualizara automaticamente cada vez que
    // el estado en el viewmodel cambie.
    val uiState by viewModel.uiState.collectAsState()

    // launchedeffect es un "oyente" que se dispara cuando
    // 'uistate.logoutcomplete' cambia a 'true'.
    // se usa para manejar la navegacion *despues* de que la logica
    // del viewmodel haya terminado.
    LaunchedEffect(key1 = uiState.logoutComplete) {
        if (uiState.logoutComplete) {
            onLogoutComplete()
        }
    }

    // delega la logica de la ui al composable "tonto".
    InfoPersonalContent(
        uiState = uiState,
        onLogoutClick = { viewModel.logout() },
        onBackPress = onBackPress
    )
}

/**
 * el "dumb composable" (tonto) que dibuja la ui de la pantalla.
 *
 * no tiene logica de negocio. solo recibe el estado ([InfoPersonalUiState])
 * y reporta los eventos (clics) a las funciones lambda.
 *
 * tambien gestiona el estado local del dialogo de confirmacion ('showdialog').
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoPersonalContent(
    uiState: InfoPersonalUiState,
    onLogoutClick: () -> Unit,
    onBackPress: () -> Unit
) {
    // estado local para controlar la visibilidad del dialogo de alerta.
    var showDialog by remember { mutableStateOf(false) }

    // logica para mostrar el dialogo de confirmacion
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // cierra si se toca fuera
            title = { Text("cerrar sesion") },
            text = { Text("¿estas seguro de que deseas cerrar la sesion?") },

            // boton de confirmar (si, cerrar sesion)
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onLogoutClick() // llama a la logica de logout
                    }
                ) {
                    Text("confirmar")
                }
            },

            // boton de descartar (no, cancelar)
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("informacion personal") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // 'spacebetween' empuja el boton de logout al fondo.
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(16.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    // contenedor para las tarjetas de informacion
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard(
                            icon = Icons.Filled.Person,
                            label = "nombre",
                            value = uiState.nombreCompleto
                        )
                        InfoCard(
                            icon = Icons.Filled.Email,
                            label = "email",
                            value = uiState.email
                        )
                        // el 'infocard' para el rut fue removido
                        // por ser un dato sensible.
                    }
                }
            }

            Button(
                // este boton no llama a 'onlogoutclick' directamente,
                // sino que activa el dialogo de confirmacion.
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "cerrar sesion",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("CERRAR SESION")
            }
        }
    }
}

/**
 * un composable privado y reutilizable para mostrar una
 * fila de informacion (icono, etiqueta y valor)
 * dentro de una [Card].
 */
@Composable
private fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}