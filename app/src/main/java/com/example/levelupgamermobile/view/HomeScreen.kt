package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.levelupgamermobile.navigation.AppScreens
import com.example.levelupgamermobile.navigation.BottomNavItem
import com.example.levelupgamermobile.navigation.ProfileNavRoutes

import com.example.levelupgamermobile.view.InfoPersonalScreen
import com.example.levelupgamermobile.view.ProfileMenuScreen
import com.example.levelupgamermobile.view.SoporteScreen
import com.example.levelupgamermobile.view.TerminosScreen
import com.example.levelupgamermobile.view.AcercaDeScreen
import com.example.levelupgamermobile.view.MisOpinionesScreen
import com.example.levelupgamermobile.view.MisTicketsScreen
import com.example.levelupgamermobile.view.PlaceholderScreen

/**
 * Pantalla contenedora principal.
 *
 * @param mainNavController El NavController "Padre" (de MainActivity)
 * @param onLogoutComplete Función para cerrar sesión (navega al Login)
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario para Scaffold y NavigationBar
@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    onLogoutComplete: () -> Unit
) {
    // (1) El NavController "anidado" o "interno"
    val nestedNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Store,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    // (2) Lógica para que "Perfil" siga seleccionado
                    // en las pantallas internas (ej. InfoPersonal)
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == screen.route ||
                                // Si la ruta actual es parte del flujo de Perfil, marca "Perfil"
                                (screen == BottomNavItem.Profile && it.route?.startsWith("profile_") == true)
                    } == true

                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = isSelected,
                        onClick = {
                            nestedNavController.navigate(screen.route) {
                                popUpTo(nestedNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // (3) Este NavHost "interno" maneja las 3 pestañas
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItem.Store.route, // Pestaña inicial: Tienda
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- Pestaña 1: Tienda ---
            composable(BottomNavItem.Store.route) {
                ProductListScreen(
                    onProductClick = { productId ->
                        // Usa el NavController "Padre" para ir a Detalle
                        mainNavController.navigate(AppScreens.productDetail(productId))
                    }
                )
            }

            // --- Pestaña 2: Carrito ---
            composable(BottomNavItem.Cart.route) {
                CartScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Pestaña 3: Perfil (El Menú) ---
            // Esta es la entrada al "sub-flujo" de perfil
            composable(BottomNavItem.Profile.route) {
                ProfileMenuScreen(
                    onNavigate = { route ->
                        nestedNavController.navigate(route)
                    }
                )
            }

            // --- (4) ¡NUEVAS RUTAS! (Las pantallas del menú de perfil) ---

            // --- Información Personal (la que antes era ProfileScreen) ---
            composable(ProfileNavRoutes.INFO_PERSONAL) {
                InfoPersonalScreen(
                    onLogoutComplete = onLogoutComplete, // Pasa la función de Logout
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Soporte (Maqueta) ---
            composable(ProfileNavRoutes.SOPORTE) {
                SoporteScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Mis Tickets (Maqueta) ---
            composable(ProfileNavRoutes.MIS_TICKETS) {
                MisTicketsScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Mis Opiniones (Lógica Local) ---
            composable(ProfileNavRoutes.MIS_OPINIONES) {
                MisOpinionesScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Términos (Maqueta) ---
            composable(ProfileNavRoutes.TERMINOS) {
                TerminosScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Acerca De (Maqueta) ---
            composable(ProfileNavRoutes.ACERCA_DE) {
                AcercaDeScreen(
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }

            // --- Mis Compras (Placeholder) ---
            composable(ProfileNavRoutes.MIS_COMPRAS) {
                PlaceholderScreen(
                    title = "Mis Compras",
                    onBackPress = { nestedNavController.popBackStack() }
                )
            }
        }
    }
}