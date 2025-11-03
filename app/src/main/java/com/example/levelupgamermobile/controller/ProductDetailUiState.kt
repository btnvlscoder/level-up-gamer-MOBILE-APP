package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena

/**
 * ¡NUEVO! El UiState ahora también incluye las reseñas
 * y la calificación promedio.
 */
data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val producto: Producto? = null,
    val error: String? = null,

    // --- CAMPOS NUEVOS ---
    val reviews: List<Resena> = emptyList(),
    val averageRating: Float = 0f
)