package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.CartItem

/**
 * Representa el estado de la pantalla del Carrito.
 */
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val total: Int = 0
)