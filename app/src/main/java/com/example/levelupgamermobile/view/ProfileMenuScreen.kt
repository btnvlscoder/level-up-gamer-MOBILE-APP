package com.example.levelupgamermobile.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.navigation.ProfileNavRoutes

/**
 * Esta es la nueva pantalla "Perfil" que muestra un menú
 * de opciones (basado en tu diseño).
 */
@Composable
fun ProfileMenuScreen(
    onNavigate: (String) -> Unit // Función para navegar
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "Mi Perfil",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // --- Lista de Opciones ---
        item {
            ProfileMenuItem(
                icon = Icons.Filled.Person,
                title = "Información Personal",
                onClick = { onNavigate(ProfileNavRoutes.INFO_PERSONAL) }
            )
        }
        item {
            ProfileMenuItem(
                icon = Icons.Filled.ShoppingBag,
                title = "Mis Compras",
                onClick = { onNavigate(ProfileNavRoutes.MIS_COMPRAS) }
            )
        }
        item {
            ProfileMenuItem(
                icon = Icons.Filled.Star,
                title = "Mis Opiniones",
                onClick = { onNavigate(ProfileNavRoutes.MIS_OPINIONES) }
            )
        }

        item { Spacer(Modifier.height(24.dp)) } // Separador

        item {
            ProfileMenuItem(
                icon = Icons.Filled.SupportAgent,
                title = "Soporte",
                onClick = { onNavigate(ProfileNavRoutes.SOPORTE) }
            )
        }
        item {
            ProfileMenuItem(
                icon = Icons.Filled.ConfirmationNumber, // Ícono para "Mis Tickets"
                title = "Mis Tickets",
                onClick = { onNavigate(ProfileNavRoutes.MIS_TICKETS) }
            )
        }
        item {
            ProfileMenuItem(
                icon = Icons.Filled.Gavel,
                title = "Términos y Condiciones",
                onClick = { onNavigate(ProfileNavRoutes.TERMINOS) }
            )
        }
        item {
            ProfileMenuItem(
                icon = Icons.Filled.Info,
                title = "Acerca de Level-Up Gamer",
                onClick = { onNavigate(ProfileNavRoutes.ACERCA_DE) }
            )
        }
    }
}

/**
 * Helper Composable para cada botón del menú
 */
@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Divider()
    }
}