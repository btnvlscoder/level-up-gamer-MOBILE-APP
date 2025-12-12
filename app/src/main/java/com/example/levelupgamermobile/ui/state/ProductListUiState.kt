package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.data.local.entity.ProductoEntity

data class ProductListUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val categoryFilter: String = "Todos",
    val allCategories: List<String> = emptyList(),
    val filteredProducts: List<ProductoEntity> = emptyList()
)
