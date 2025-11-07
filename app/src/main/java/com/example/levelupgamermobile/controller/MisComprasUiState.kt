package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Purchase

/**
 * define la estructura de estado para la pantalla "mis compras".
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class MisComprasUiState(
    /**
     * indica si el viewmodel esta cargando el historial de compras.
     * la vista (view) usa esto para mostrar un indicador de carga.
     * el valor por defecto es true.
     */
    val isLoading: Boolean = true,

    /**
     * la lista del historial de compras (vouchers) del usuario.
     * el valor por defecto es una lista vacia.
     */
    val purchases: List<Purchase> = emptyList()
)