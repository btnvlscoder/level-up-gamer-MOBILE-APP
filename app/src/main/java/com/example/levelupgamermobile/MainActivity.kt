package com.example.levelupgamermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupgamermobile.navigation.AppScreens
import com.example.levelupgamermobile.ui.theme.LevelUpGamerMobileTheme
import com.example.levelupgamermobile.view.CartScreen
import com.example.levelupgamermobile.view.LoginScreen
import com.example.levelupgamermobile.view.ProductDetailScreen
import com.example.levelupgamermobile.view.ProductListScreen
import com.example.levelupgamermobile.view.RegisterScreen // ¡Importa la nueva pantalla!

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpGamerMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController: NavHostController = rememberNavController()

                    // (1) ¡CAMBIO MINUCIOSO!
                    // El `NavHost` ahora inicia en LOGIN,
                    // no en PRODUCT_LIST.
                    NavHost(
                        navController = navController,
                        startDestination = AppScreens.LOGIN
                    ) {

                        // --- Ruta 1: Login ---
                        composable(route = AppScreens.LOGIN) {
                            LoginScreen(
                                // (viewModel() usará el valor por defecto)
                                onLoginSuccess = {
                                    // ¡Navegación exitosa!
                                    navController.navigate(AppScreens.PRODUCT_LIST) {
                                        // ¡MUY IMPORTANTE!
                                        // Borra el historial de navegación
                                        // para que el usuario no pueda "volver"
                                        // a la pantalla de Login.
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    // Navega a la pantalla de registro
                                    navController.navigate(AppScreens.REGISTER)
                                }
                            )
                        }

                        // --- Ruta 2: Registro ---
                        composable(route = AppScreens.REGISTER) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    // Si el registro es exitoso,
                                    // lo mandamos a la lista de productos
                                    // (igual que el login).
                                    navController.navigate(AppScreens.PRODUCT_LIST) {
                                        // Borra el historial (Login y Register)
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onBackClick = {
                                    // Simplemente vuelve atrás (al Login)
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- Ruta 3: Lista de Productos ---
                        composable(route = AppScreens.PRODUCT_LIST) {
                            ProductListScreen(
                                onProductClick = { productId ->
                                    navController.navigate(
                                        AppScreens.productDetail(productId)
                                    )
                                },
                                onCartClick = {
                                    navController.navigate(AppScreens.CART)
                                }
                            )
                        }

                        // --- Ruta 4: Detalle de Producto ---
                        composable(
                            route = AppScreens.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("productId") {
                                type = NavType.StringType
                            })
                        ) {
                            ProductDetailScreen(
                                onBackPress = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- Ruta 5: Carrito ---
                        composable(route = AppScreens.CART) {
                            CartScreen(
                                onBackPress = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}