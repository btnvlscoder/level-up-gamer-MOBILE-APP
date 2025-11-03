package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena

/**
 * El "molde" de estado para la pantalla de detalle.
 * Incluye el producto, las reseñas y la calificación promedio.
 */
data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val producto: Producto? = null,
    val error: String? = null,

    // --- CAMPOS PARA RESEÑAS ---
    val reviews: List<Resena> = emptyList(),
    val averageRating: Float = 0f
)