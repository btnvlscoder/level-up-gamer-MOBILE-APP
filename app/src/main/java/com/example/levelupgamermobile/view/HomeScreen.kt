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
import androidx.navigation.navigation
import com.example.levelupgamermobile.navigation.AppScreens
import com.example.levelupgamermobile.navigation.BottomNavItem
import com.example.levelupgamermobile.navigation.ProfileNavRoutes
// imports de todas tus pantallas
import com.example.levelupgamermobile.view.CartScreen
import com.example.levelupgamermobile.view.InfoPersonalScreen
import com.example.levelupgamermobile.view.MisComprasScreen
import com.example.levelupgamermobile.view.MisOpinionesScreen
import com.example.levelupgamermobile.view.MisTicketsScreen
import com.example.levelupgamermobile.view.PlaceholderScreen
import com.example.levelupgamermobile.view.ProductListScreen
import com.example.levelupgamermobile.view.ProfileMenuScreen
import com.example.levelupgamermobile.view.SoporteScreen
import com.example.levelupgamermobile.view.VoucherScreen
import com.example.levelupgamermobile.view.AcercaDeScreen
import com.example.levelupgamermobile.view.TerminosScreen

/**
 * la pantalla contenedora principal de la app (post-login).
 *
 * esta pantalla es responsable de la navegacion principal
 * mediante la barra inferior (bottom navigation bar).
 *
 * implementa una navegacion anidada:
 * - [mainNavController] (de [MainActivity]) se usa para navegar "fuera"
 * (a [ProductDetailScreen] o al logout).
 * - [nestedNavController] (local) se usa para navegar "dentro" de las pestanas.
 *
 * @param mainNavController el controlador de navegacion "padre" (de [MainActivity]).
 * @param onLogoutComplete funcion lambda que se ejecuta para volver al [LoginScreen].
 */
@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    onLogoutComplete: () -> Unit
) {
    // controlador de navegacion "anidado" o "interno"
    // para gestionar las pestanas.
    val nestedNavController = rememberNavController()

    // define los items de la barra de navegacion
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
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        // logica de seleccion:
                        // la pestana se marca como activa si la ruta actual
                        // comienza con la ruta raiz de la pestana
                        // (ej. "profile_tab/info_personal" activa "profile_tab").
                        selected = currentDestination?.hierarchy?.any {
                            it.route?.startsWith(screen.route) == true
                        } == true,
                        onClick = {
                            // logica de navegacion interna para las pestanas.
                            nestedNavController.navigate(screen.route) {
                                // evita acumular historial al cambiar de pestana.
                                popUpTo(nestedNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // restaura el estado al volver (ej. scroll).
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // navhost "interno" que dibuja la pantalla de la pestana seleccionada.
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItem.Store.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- pestana 1: tienda ---
            composable(BottomNavItem.Store.route) { // "store_tab"
                ProductListScreen(
                    onProductClick = { productId ->
                        // usa el [mainNavController] (padre) para navegar a una pantalla
                        // que cubra toda la app (incluyendo la barra inferior).
                        mainNavController.navigate(AppScreens.productDetail(productId))
                    }
                )
            }

            // --- pestana 2: carrito ---
            composable(BottomNavItem.Cart.route) { // "cart_tab"
                CartScreen(
                    onBackPress = { nestedNavController.popBackStack() },
                    onComprarClick = {
                        // usa el [nestedNavController] (hijo) para navegar
                        // "dentro" del flujo del carrito.
                        nestedNavController.navigate("voucher")
                    }
                )
            }

            // --- ruta del voucher (parte del flujo del carrito) ---
            composable("voucher") {
                VoucherScreen(
                    onFinalizarClick = {
                        // resetea el grafo de navegacion interno a la tienda.
                        nestedNavController.navigate(BottomNavItem.Store.route) {
                            popUpTo(nestedNavController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // --- pestana 3: perfil (grafo de navegacion anidado) ---
            // define un sub-grafo completo para la pestana de perfil.
            // esto agrupa todas las pantallas de perfil bajo la ruta 'profile_tab'.
            navigation(
                startDestination = ProfileNavRoutes.MENU, // la primera pantalla de este grupo
                route = BottomNavItem.Profile.route // la ruta de la pestana
            ) {

                composable(ProfileNavRoutes.MENU) {
                    ProfileMenuScreen(
                        onNavigate = { route ->
                            nestedNavController.navigate(route)
                        }
                    )
                }

                composable(ProfileNavRoutes.INFO_PERSONAL) {
                    InfoPersonalScreen(
                        onLogoutComplete = onLogoutComplete,
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                // --- soporte (el formulario real) ---
                composable(ProfileNavRoutes.SOPORTE) {
                    SoporteScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                // --- mis tickets (la maqueta) ---
                composable(ProfileNavRoutes.MIS_TICKETS) {
                    MisTicketsScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                composable(ProfileNavRoutes.MIS_COMPRAS) {
                    MisComprasScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                composable(ProfileNavRoutes.MIS_OPINIONES) {
                    MisOpinionesScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                composable(ProfileNavRoutes.TERMINOS) {
                    TerminosScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                composable(ProfileNavRoutes.ACERCA_DE) {
                    AcercaDeScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }
            }
        }
    }
}