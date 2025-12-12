package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity

data class CartUiState(
    val items: List<CarritoItemEntity> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
