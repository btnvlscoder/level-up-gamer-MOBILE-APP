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
// Imports de pantallas
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

@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    onLogoutComplete: () -> Unit
) {
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
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route?.startsWith(screen.route) == true
                        } == true,
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
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItem.Store.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- Pestaña 1: Tienda ---
            composable(BottomNavItem.Store.route) {
                ProductListScreen(
                    onProductClick = { productId ->
                        mainNavController.navigate(AppScreens.productDetail(productId))
                    }
                )
            }

            // --- Pestaña 2: Carrito ---
            composable(BottomNavItem.Cart.route) {
                CartScreen(
                    onBackPress = { nestedNavController.popBackStack() },
                    onComprarClick = {
                        nestedNavController.navigate("voucher")
                    }
                )
            }

            // --- Ruta del Voucher ---
            composable("voucher") {
                VoucherScreen(
                    onFinalizarClick = {
                        nestedNavController.navigate(BottomNavItem.Store.route) {
                            popUpTo(nestedNavController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // --- Pestaña 3: Perfil (Grafo anidado) ---
            navigation(
                startDestination = ProfileNavRoutes.MENU,
                route = BottomNavItem.Profile.route
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

                composable(ProfileNavRoutes.SOPORTE) {
                    SoporteScreen(
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

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

                // V--- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---V
                composable(ProfileNavRoutes.TERMINOS) {
                    TerminosScreen( // <-- ¡Ya no es Placeholder!
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }

                composable(ProfileNavRoutes.ACERCA_DE) {
                    AcercaDeScreen( // <-- ¡Ya no es Placeholder!
                        onBackPress = { nestedNavController.popBackStack() }
                    )
                }
                // A--- FIN DE LA CORRECCIÓN ---A
            }
        }
    }
}