package com.example.levelupgamermobile.navigation

/**
 * define las rutas de navegacion "internas" (anidadas)
 * para la pestana de perfil.
 *
 * este objeto 'object' (singleton) actua como la unica fuente
 * de verdad para las rutas del [NavHost] anidado
 * dentro de [HomeScreen].
 *
 * [ProfileMenuScreen] usa estas constantes para navegar
 * a las diferentes sub-pantallas.
 */
object ProfileNavRoutes {

    // la pantalla del menu principal (la primera de este grafo anidado)
    const val MENU = "profile_menu"

    // las pantallas a las que navega el menu
    const val INFO_PERSONAL = "info_personal"
    const val MIS_COMPRAS = "mis_compras"
    const val MIS_OPINIONES = "mis_opiniones"
    const val MIS_TICKETS = "mis_tickets"
    const val SOPORTE = "soporte"
    const val TERMINOS = "terminos"
    const val ACERCA_DE = "acerca_de"
}