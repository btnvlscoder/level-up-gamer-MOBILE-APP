package com.example.levelupgamermobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Esta es una "clase sellada" (sealed class).
 * Es como un "enum" (enumeración) pero más potente.
 * La usamos para definir una lista fija de nuestras
 * pestañas de navegación.
 */
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    // Pestaña 1: Tienda
    object Store : BottomNavItem(
        title = "Tienda",
        icon = Icons.Filled.Store,
        route = "store" // Esta es una ruta interna
    )

    // Pestaña 2: Carrito
    object Cart : BottomNavItem(
        title = "Carrito",
        icon = Icons.Filled.ShoppingCart,
        route = "cart" // Esta ruta coincide con la de AppScreens
    )

    // Pestaña 3: Perfil
    object Profile : BottomNavItem(
        title = "Perfil",
        icon = Icons.Filled.Person,
        route = "profile" // Ruta interna
    )
}