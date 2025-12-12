package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
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
import com.example.levelupgamermobile.ui.screens.CartScreen
import com.example.levelupgamermobile.ui.screens.InfoPersonalScreen
import com.example.levelupgamermobile.ui.screens.MisComprasScreen
import com.example.levelupgamermobile.ui.screens.MisOpinionesScreen
import com.example.levelupgamermobile.ui.screens.MisTicketsScreen
import com.example.levelupgamermobile.ui.screens.ProductListScreen
import com.example.levelupgamermobile.ui.screens.ProfileMenuScreen
import com.example.levelupgamermobile.ui.screens.SoporteScreen
import com.example.levelupgamermobile.ui.screens.VoucherScreen
import com.example.levelupgamermobile.ui.screens.AcercaDeScreen
import com.example.levelupgamermobile.ui.screens.TerminosScreen
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainNavController: NavHostController,
    onLogoutComplete: () -> Unit,
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val nestedNavController = rememberNavController()
    val cartState by cartViewModel.uiState.collectAsState()
    val cartCount = cartState.items.sumOf { it.cantidad }

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
                        icon = { 
                            if (screen == BottomNavItem.Cart && cartCount > 0) {
                                BadgedBox(
                                    badge = { Badge { Text(cartCount.toString()) } }
                                ) {
                                    Icon(screen.icon, contentDescription = screen.title)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = screen.title)
                            }
                        },
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
                // Botón de la cámara
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Camera, contentDescription = "Camera") },
                    label = { Text("Camera") },
                    selected = false,
                    onClick = { mainNavController.navigate(AppScreens.CAMERA) }
                )
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
                    viewModel = cartViewModel,
                    onBackPress = { nestedNavController.popBackStack() },
                    onComprarClick = {
                        nestedNavController.navigate("voucher")
                    }
                )
            }

            // --- Ruta del Voucher ---
            composable("voucher") {
                VoucherScreen(
                    viewModel = cartViewModel,
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