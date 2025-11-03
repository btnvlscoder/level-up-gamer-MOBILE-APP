package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Producto

/**
 * El "estado" de la pantalla de detalle.
 */
data class ProductDetailUiState(
    val isLoading: Boolean = true,

    // Usamos un "Producto?" (con el signo ?)
    // para indicar que el producto puede ser "null"
    // (por ejemplo, si no se encuentra o aún no se carga).
    val producto: Producto? = null,
    val error: String? = null
)