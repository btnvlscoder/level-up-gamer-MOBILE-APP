package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena

/**
 * Representa los datos que la pantalla "Mis Opiniones" necesita.
 *
 * (Usaremos un 'Pair' para mostrar la reseña junto
 * con la información del producto al que pertenece).
 */
data class MisOpinionesUiState(
    val isLoading: Boolean = true,
    val opiniones: List<Pair<Resena, Producto?>> = emptyList()
)