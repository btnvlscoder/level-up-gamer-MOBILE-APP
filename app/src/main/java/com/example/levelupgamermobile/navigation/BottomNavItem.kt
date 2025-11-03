package com.example.levelupgamermobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define las 3 pestañas principales de la barra de navegación.
 */
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String // La ruta "raíz" de esta pestaña
) {
    object Store : BottomNavItem(
        title = "Tienda",
        icon = Icons.Filled.Store,
        route = "store_tab" // Cambiado para ser único
    )

    object Cart : BottomNavItem(
        title = "Carrito",
        icon = Icons.Filled.ShoppingCart,
        route = "cart_tab" // Cambiado para ser único
    )

    object Profile : BottomNavItem(
        title = "Perfil",
        icon = Icons.Filled.Person,
        route = "profile_tab" // Cambiado para ser único
    )
}