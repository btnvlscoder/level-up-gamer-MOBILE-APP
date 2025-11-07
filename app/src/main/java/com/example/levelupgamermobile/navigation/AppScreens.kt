package com.example.levelupgamermobile.navigation

/**
 * define las rutas de navegacion "principales" (padre) de la aplicacion.
 *
 * este objeto 'object' (singleton) actua como la unica fuente
 * de verdad para las rutas del [NavHost] principal en [MainActivity].
 *
 * las rutas de las pestañas (ej. tienda, carrito) no estan aqui,
 * ya que son manejadas por un [NavHost] anidado dentro de [HomeScreen].
 */
object AppScreens {

    /**
     * rutas para el flujo de autenticacion.
     * estas pantallas se muestran *antes* de entrar a [HomeScreen].
     */
    const val LOGIN = "login"
    const val REGISTER = "register"

    /**
     * la ruta al contenedor principal de la app (el [Scaffold]
     * con la barra de navegacion inferior).
     */
    const val HOME = "home"

    /**
     * ruta para el detalle de producto.
     * esta ruta se define aqui (en el [NavHost] principal)
     * porque se muestra "encima" de [HomeScreen], ocultando
     * la barra de navegacion inferior.
     *
     * incluye un argumento dinamico '{productid}'.
     */
    const val PRODUCT_DETAIL = "product_detail/{productId}"

    /**
     * funcion ayudante (helper) para construir la ruta de detalle
     * de forma segura, asegurando que el 'productid' se inserte
     * correctamente.
     *
     * @param productId el 'codigo' del producto a navegar.
     * @return el string de la ruta completa (ej. "product_detail/jm001").
     */
    fun productDetail(productId: String): String {
        return "product_detail/$productId"
    }
}