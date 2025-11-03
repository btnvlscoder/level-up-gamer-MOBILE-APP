package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamermobile.navigation.BottomNavItem

/**
 * Esta es la pantalla CONTENEDORA principal.
 * Maneja su propia navegación interna (las pestañas).
 *
 * @param mainNavController El controlador de navegación "principal"
 * (de MainActivity) para poder navegar "fuera" de esta pantalla
 * (ej. al hacer logout o al ir al detalle de un producto).
 */
@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    onLogoutComplete: () -> Unit
) {
    // (1) ¡NUEVO NavController!
    // Este es el controlador "anidado" o "interno"
    // que manejará SÓLO las pestañas (Tienda, Carrito, Perfil).
    val nestedNavController = rememberNavController()

    // (2) Lista de nuestras pestañas
    val items = listOf(
        BottomNavItem.Store,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    Scaffold(
        // (3) La Barra de Navegación Inferior
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // (4) Creamos un botón por cada item en nuestra lista
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            // (5) Lógica de navegación interna
                            nestedNavController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(nestedNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Evita múltiples copias de la misma pestaña
                                launchSingleTop = true
                                // Restaura el estado al volver a seleccionar
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // (6) ¡NUEVO NavHost!
        // Este es el NavHost "interno" que dibuja la pantalla
        // correspondiente a la pestaña seleccionada.
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItem.Store.route, // Empezamos en la Tienda
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- Pestaña 1: Tienda ---
            composable(BottomNavItem.Store.route) {
                ProductListScreen(
                    // ¡OJO! Pasamos el NavController PRINCIPAL
                    // para que pueda navegar al detalle del producto.
                    onProductClick = { productId ->
                        mainNavController.navigate("product_detail/$productId")
                    },
                    // ¡OJO! Le decimos que cambie a la PESTAÑA de Carrito
                    onCartClick = {
                        nestedNavController.navigate(BottomNavItem.Cart.route)
                    }
                )
            }

            // --- Pestaña 2: Carrito ---
            composable(BottomNavItem.Cart.route) {
                CartScreen(
                    // Le decimos que vuelva a la pestaña anterior
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Pestaña 3: Perfil ---
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    // Le pasamos la función de logout
                    // que viene desde MainActivity
                    onLogoutComplete = onLogoutComplete
                )
            }
        }
    }
}