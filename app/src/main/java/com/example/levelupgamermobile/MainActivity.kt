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
import com.example.levelupgamermobile.view.HomeScreen
import com.example.levelupgamermobile.view.LoginScreen
import com.example.levelupgamermobile.view.ProductDetailScreen
import com.example.levelupgamermobile.view.RegisterScreen

/**
 * la [Activity] principal y el punto de entrada unico de la aplicacion.
 *
 * esta clase es responsable de:
 * 1. inicializar el tema de la app ([LevelUpGamerMobileTheme]).
 * 2. configurar el [NavHost] "principal" (padre).
 *
 * el [NavHost] principal gestiona la navegacion entre los flujos
 * "fuera" de la app (autenticacion) y los flujos "dentro" de la app ([HomeScreen]).
 */
class MainActivity : ComponentActivity() {

    /**
     * se llama cuando la [Activity] se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. [installSplashScreen] debe llamarse *antes* de [super.onCreate]
        //    para manejar correctamente la transicion desde la pantalla de carga.
        installSplashScreen()

        // 2. se llama al metodo de la superclase.
        super.onCreate(savedInstanceState)

        // 3. 'setcontent' construye la ui de jetpack compose.
        setContent {
            LevelUpGamerMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // este es el controlador de navegacion "padre".
                    // se pasa a [HomeScreen] para que pueda navegar a pantallas
                    // "externas" (como [ProductDetailScreen]).
                    val navController: NavHostController = rememberNavController()

                    // este es el grafo de navegacion "principal" de la app.
                    NavHost(
                        navController = navController,
                        startDestination = AppScreens.LOGIN
                    ) {

                        // --- 1. ruta de login ---
                        // es el 'startdestination' (punto de inicio) de la app.
                        composable(
                            route = AppScreens.LOGIN,
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            }
                        ) {
                            LoginScreen(
                                onLoginSuccess = {
                                    // al hacer login exitoso: navega a 'home'.
                                    navController.navigate(AppScreens.HOME) {
                                        // 'popupto(login)' borra el historial para que
                                        // el usuario no pueda "volver" al login.
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate(AppScreens.REGISTER)
                                }
                            )
                        }

                        // --- 2. ruta de registro ---
                        composable(
                            route = AppScreens.REGISTER,
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        ) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    // al registrarse: navega a 'home'.
                                    navController.navigate(AppScreens.HOME) {
                                        // borra todo el historial de autenticacion.
                                        popUpTo(AppScreens.LOGIN) { inclusive = true }
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- 3. ruta de home (el contenedor principal) ---
                        // esta pantalla contiene su propio [NavHost] anidado.
                        composable(
                            route = AppScreens.HOME,
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            },
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                            }
                        ) {
                            HomeScreen(
                                mainNavController = navController,
                                onLogoutComplete = {
                                    // al cerrar sesion: navega de vuelta a 'login'.
                                    navController.navigate(AppScreens.LOGIN) {
                                        // borra 'home' del historial.
                                        popUpTo(AppScreens.HOME) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // --- 4. ruta de detalle de producto ---
                        // esta ruta se define aqui (en el navhost padre)
                        // y no en [HomeScreen], porque debe mostrarse
                        // *encima* de la barra de navegacion inferior.
                        composable(
                            route = AppScreens.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("productId") {
                                type = NavType.StringType
                            }),
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                            },
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                            },
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
                    }
                }
            }
        }
    }
}