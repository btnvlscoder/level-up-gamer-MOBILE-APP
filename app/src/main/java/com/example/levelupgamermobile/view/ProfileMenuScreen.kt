package com.example.levelupgamermobile.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Build // Para Soporte
import androidx.compose.material.icons.filled.ConfirmationNumber // Para Mis Tickets
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.navigation.ProfileNavRoutes

// (1) Define los ítems del menú
private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

// (2) ¡LISTA CORREGIDA!
// Ahora tiene 7 items, con "Mis Tickets" y "Soporte" separados.
private val menuItems = listOf(
    MenuItem("Información Personal", Icons.Filled.Person, ProfileNavRoutes.INFO_PERSONAL),
    MenuItem("Mis Compras", Icons.Filled.ShoppingCart, ProfileNavRoutes.MIS_COMPRAS),
    MenuItem("Mis Opiniones", Icons.Filled.Feedback, ProfileNavRoutes.MIS_OPINIONES),
    MenuItem("Mis Tickets", Icons.Filled.ConfirmationNumber, ProfileNavRoutes.MIS_TICKETS),
    MenuItem("Soporte", Icons.Filled.Build, ProfileNavRoutes.SOPORTE),
    MenuItem("Términos y Condiciones", Icons.Filled.Description, ProfileNavRoutes.TERMINOS),
    MenuItem("Acerca de Level-Up Gamer", Icons.Filled.Info, ProfileNavRoutes.ACERCA_DE)
)

/**
 * Pantalla principal del menú de perfil.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(menuItems) { item ->
                ProfileMenuItem(
                    title = item.title,
                    icon = item.icon,
                    onClick = { onNavigate(item.route) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/**
 * Un Composable reutilizable para una fila del menú.
 */
@Composable
private fun ProfileMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "Ir",
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}