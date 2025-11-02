package com.example.levelupgamermobile.navigation

/**
 * Usamos "object" (Singleton) para guardar nuestras rutas
 * como constantes. Es más seguro que escribir "list" o "detail"
 * a mano en todos lados.
 */
object AppScreens {
    const val PRODUCT_LIST = "product_list"

    // Esta es una ruta "dinámica".
    // "{productId}" significa que esperamos un argumento
    // que se llamará "productId".
    const val PRODUCT_DETAIL = "product_detail/{productId}"

    // Esta es una función "helper" (ayudante)
    // para construir la ruta de detalle fácilmente.
    fun productDetail(productId: String): String {
        return "product_detail/$productId"
    }
}