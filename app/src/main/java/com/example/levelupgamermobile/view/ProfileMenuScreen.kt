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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ConfirmationNumber
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

// data class privada para definir la estructura de un item del menu.
private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

// lista estatica de los items que se mostraran en el menu.
// cada item define su ruta de [ProfileNavRoutes].
private val menuItems = listOf(
    MenuItem("informacion personal", Icons.Filled.Person, ProfileNavRoutes.INFO_PERSONAL),
    MenuItem("mis compras", Icons.Filled.ShoppingCart, ProfileNavRoutes.MIS_COMPRAS),
    MenuItem("mis opiniones", Icons.Filled.Feedback, ProfileNavRoutes.MIS_OPINIONES),
    MenuItem("mis tickets", Icons.Filled.ConfirmationNumber, ProfileNavRoutes.MIS_TICKETS),
    MenuItem("soporte", Icons.Filled.Build, ProfileNavRoutes.SOPORTE),
    MenuItem("terminos y condiciones", Icons.Filled.Description, ProfileNavRoutes.TERMINOS),
    MenuItem("acerca de level-up gamer", Icons.Filled.Info, ProfileNavRoutes.ACERCA_DE)
)

/**
 * la pantalla principal del menu de perfil.
 *
 * esta es la primera pantalla que el usuario ve al seleccionar
 * la pestana "perfil" en la [NavigationBar] de [HomeScreen].
 *
 * su unica responsabilidad es mostrar una lista de opciones
 * y delegar la accion de navegacion (clic) a su llamador
 * (el [NavHost] anidado en [HomeScreen]).
 *
 * @param onNavigate funcion lambda que se llama con la ruta
 * de destino (ej. "info_personal") cuando un item es presionado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("mi perfil", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { paddingValues ->
        // 'lazycolumn' se usa para listas, ya que es mas eficiente
        // que un 'column' simple, especialmente si la lista crece.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(menuItems) { item ->
                ProfileMenuItem(
                    title = item.title,
                    icon = item.icon,
                    onClick = { onNavigate(item.route) } // delega la navegacion
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/**
 * un composable privado y reutilizable para dibujar
 * una unica fila (item) del menu de perfil.
 *
 * @param title el texto a mostrar (ej. "mis compras").
 * @param icon el [ImageVector] a mostrar a la izquierda.
 * @param onClick la accion (lambda) a ejecutar cuando se presiona la fila.
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
            .clickable(onClick = onClick) // hace que toda la fila sea clickeable
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // empuja la flecha al final
    ) {
        // contenedor para el icono y el texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary // usa el color azul
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        // icono de flecha a la derecha
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "ir",
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}