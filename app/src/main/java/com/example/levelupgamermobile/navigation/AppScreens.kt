package com.example.levelupgamermobile.navigation

/**
 * Define las rutas de navegación "principales"
 * (las que maneja MainActivity).
 *
 * PRODUCT_LIST y CART ya no están aquí porque ahora son
 * rutas "internas" manejadas por HomeScreen.
 */
object AppScreens {

    // Flujo de Autenticación
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Flujo Principal de la App (el contenedor)
    const val HOME = "home"

    // Pantalla de "encima" (se muestra sobre HOME)
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val CAMERA = "camera" // Nueva ruta para la cámara

    // Función helper (ayudante)
    fun productDetail(productId: String): String {
        return "product_detail/$productId"
    }
}