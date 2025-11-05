package com.example.levelupgamermobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * define los items fijos de la barra de navegacion inferior.
 *
 * usamos una 'sealed class' (clase sellada) para crear una lista
 * estatica y conocida de las pestanas principales de la app.
 *
 * [HomeScreen] usa esta clase para construir la [NavigationBar]
 * y su [NavHost] anidado.
 *
 * @param title el texto que se muestra debajo del icono (ej. "tienda").
 * @param icon el [ImageVector] de material icons (ej. icons.filled.store).
 * @param route la ruta "raiz" unica para esta pestana.
 */
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    /**
     * la pestana de la tienda (catalogo).
     */
    object Store : BottomNavItem(
        title = "tienda",
        icon = Icons.Filled.Store,
        route = "store_tab"
    )

    /**
     * la pestana del carrito de compras.
     */
    object Cart : BottomNavItem(
        title = "carrito",
        icon = Icons.Filled.ShoppingCart,
        route = "cart_tab"
    )

    /**
     * la pestana del menu de perfil del usuario.
     */
    object Profile : BottomNavItem(
        title = "perfil",
        icon = Icons.Filled.Person,
        route = "profile_tab"
    )
}