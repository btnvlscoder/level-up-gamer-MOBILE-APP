package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.CartItem

/**
 * define la estructura de estado para la pantalla del carrito.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class CartUiState(
    /**
     * la lista actual de productos en el carrito.
     * el valor por defecto es una lista vacia.
     */
    val items: List<CartItem> = emptyList(),

    /**
     * el costo total calculado de todos los items.
     * el valor por defecto es 0.
     */
    val total: Int = 0
)