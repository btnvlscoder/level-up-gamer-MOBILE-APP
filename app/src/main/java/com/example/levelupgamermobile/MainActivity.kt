package com.example.levelupgamermobile
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.example.levelupgamermobile.ui.screens.CameraScreen
import com.example.levelupgamermobile.ui.screens.HomeScreen
import com.example.levelupgamermobile.ui.screens.LoginScreen
import com.example.levelupgamermobile.ui.screens.ProductDetailScreen
import com.example.levelupgamermobile.ui.screens.RegisterScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // (1) Instalar GlobalExceptionHandler
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler(applicationContext, defaultHandler))
        
        installSplashScreen()
        setContent {
            LevelUpGamerMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // (1) Este es el NavController "Principal" o "Padre"
                    val navController: NavHostController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = AppScreens.LOGIN
                    ) {

                        // --- Ruta 1: Login ---
                        composable(
                            route = AppScreens.LOGIN,
                            // Animación al salir de esta pantalla
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            // Animación al volver a esta pantalla (ej. al hacer logout)
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            }
                        ) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(AppScreens.HOME) {
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate(AppScreens.REGISTER)
                                }
                            )
                        }

                        // --- Ruta 2: Registro ---
                        composable(route = AppScreens.REGISTER,
                            // Animación al entrar a esta pantalla
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            // Animación al salir (al presionar "atrás")
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        ) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    // Navega a HOME
                                    navController.navigate(AppScreens.HOME) {
                                        // Borra Login y Register de la pila
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- Ruta 3: Home (El Contenedor) ---
                        composable(
                            route = AppScreens.HOME,
                            // (Desde Login/Register) Entra desde la derecha
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            // (Hacia Detalle) Sale hacia la izquierda
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            // (Volviendo desde Detalle) Entra desde la izquierda
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            },
                            // (Haciendo Logout) Sale hacia la derecha
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        ) {
                            HomeScreen(
                                mainNavController = navController, // Le pasamos el control "Padre"
                                onLogoutComplete = {
                                    // Lógica de Logout
                                    navController.navigate(AppScreens.LOGIN) {
                                        // Borra HOME de la pila
                                        popUpTo(AppScreens.HOME) { inclusive = true }
                                    }
                                }
                            )
                        }

                        //--- Ruta 4: Detalle de Producto ---
                        composable(
                            route = AppScreens.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("productId") {
                                type = NavType.StringType
                            }),
                            // (Desde Home) Entra desde la derecha
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            // (Hacia otra pantalla) Sale hacia la izquierda
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            // (Volviendo desde otra pantalla) Entra desde la izquierda
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            },
                            // (Volviendo a Home) Sale hacia la derecha
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        ) {
                            ProductDetailScreen(
                                onBackPress = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- Ruta 5: Cámara ---
                        composable(route = AppScreens.CAMERA) {
                            CameraScreen(onImageCaptured = { uri ->
                                // Manejar la URI de la imagen capturada
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}