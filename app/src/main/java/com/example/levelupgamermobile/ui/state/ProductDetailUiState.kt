package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.data.local.entity.ResenaEntity

data class ProductDetailUiState(
    val producto: ProductoEntity? = null,
    val reviews: List<ResenaEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
