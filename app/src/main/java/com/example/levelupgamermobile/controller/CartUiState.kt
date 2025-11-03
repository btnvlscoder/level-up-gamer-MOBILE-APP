package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.CartItem

/**
 * Representa el estado de la pantalla del Carrito.
 */
data class CartUiState(
    // La lista de items en el carrito.
    val items: List<CartItem> = emptyList(),
    // El precio total calculado.
    val total: Int = 0
)