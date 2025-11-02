package com.example.levelupgamermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupgamermobile.navigation.AppScreens
import com.example.levelupgamermobile.ui.theme.LevelUpGamerMobileTheme
import com.example.levelupgamermobile.view.ProductDetailScreen
import com.example.levelupgamermobile.view.ProductListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpGamerMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // (1) Creamos el controlador de navegación
                    // "rememberNavController" crea y "recuerda" el
                    // controlador que gestiona nuestras pantallas.
                    val navController: NavHostController = rememberNavController()

                    // (2) Creamos el "Anfitrión" de Navegación
                    // "NavHost" es el contenedor que cambiará las pantallas.
                    NavHost(
                        navController = navController,
                        // Le decimos cuál es nuestra pantalla inicial
                        startDestination = AppScreens.PRODUCT_LIST
                    ) {

                        // --- Ruta 1: La Lista de Productos ---
                        composable(route = AppScreens.PRODUCT_LIST) {
                            // Llamamos a nuestra pantalla de lista
                            ProductListScreen(
                                viewModel = viewModel(),
                                // ¡NUEVO! Le pasamos una "función"
                                // que se ejecutará cuando se haga clic
                                // en un producto.
                                onProductClick = { productId ->
                                    // Le decimos al "navController" que navegue
                                    // a la ruta de detalle.
                                    navController.navigate(
                                        AppScreens.productDetail(productId)
                                    )
                                }
                            )
                        }

                        // --- Ruta 2: El Detalle del Producto ---
                        composable(
                            route = AppScreens.PRODUCT_DETAIL,
                            // Le decimos que esta ruta espera un argumento
                            arguments = listOf(navArgument("productId") {
                                type = NavType.StringType
                            })
                        ) {
                            // Llamamos a nuestra pantalla de detalle
                            ProductDetailScreen(
                                // El ViewModel se crea solo y es
                                // lo bastante inteligente para tomar
                                // el "productId" de la ruta.
                                viewModel = viewModel(),
                                // Le pasamos la función para "volver atrás"
                                onBackPress = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // (Aquí irán nuestras futuras rutas:
                        // Login, Carrito, Contacto, etc.)
                    }
                }
            }
        }
    }
}