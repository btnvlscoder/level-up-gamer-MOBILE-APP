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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.ProfileUiState
import com.example.levelupgamermobile.controller.ProfileViewModel
/**
 * Pantalla "inteligente" de Perfil.
 *
 * @param onLogoutComplete Lambda que se ejecuta cuando el
 * logout es exitoso, para navegar de vuelta al Login.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLogoutComplete: () -> Unit
) {
    // (1) Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // (2) LaunchedEffect para el evento de logout
    LaunchedEffect(key1 = uiState.logoutComplete) {
        if (uiState.logoutComplete) {
            onLogoutComplete() // ¡Navega!
        }
    }

    // (3) Llamamos a la pantalla "tonta"
    ProfileContent(
        uiState = uiState,
        onLogoutClick = { viewModel.logout() } // Conectamos el botón
    )
}

/**
 * Pantalla "tonta" (Dumb Composable) de Perfil.
 * Solo muestra la UI.
 */
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        // (Podríamos añadir un TopAppBar si quisiéramos)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Empuja el logout al fondo
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Mi Perfil",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // (4) Si está cargando (raro, pero por si acaso)
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    // (5) Mostramos la información del usuario
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard(
                            icon = Icons.Filled.Person,
                            label = "Nombre",
                            value = uiState.nombre
                        )
                        InfoCard(
                            icon = Icons.Filled.Email,
                            label = "Email",
                            value = uiState.email
                        )
                        InfoCard(
                            icon = Icons.Filled.Person, // (Podrías cambiar este ícono)
                            label = "RUT",
                            value = uiState.rut
                        )
                    }
                }
            }

            // (6) Botón de Cerrar Sesión
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("CERRAR SESIÓN")
            }
        }
    }
}

/**
 * Un Composable reutilizable para mostrar una fila de información.
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
                Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}