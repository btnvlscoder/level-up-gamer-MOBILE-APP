package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Purchase

/**
 * Representa el estado de la pantalla "Mis Compras".
 */
data class MisComprasUiState(
    val isLoading: Boolean = true,
    val purchases: List<Purchase> = emptyList()
)