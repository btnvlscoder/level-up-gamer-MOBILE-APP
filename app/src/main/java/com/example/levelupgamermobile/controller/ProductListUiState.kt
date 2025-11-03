package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Producto

/**
 * Estado actualizado para la pantalla de catálogo.
 * (Se eliminó "userName").
 */
data class ProductListUiState(
    val isLoading: Boolean = true,

    // Filtros
    val searchQuery: String = "",
    val categoryFilter: String = "Todos",

    // Listas
    val allCategories: List<String> = emptyList(), // Para el menú desplegable
    val filteredProducts: List<Producto> = emptyList() // La lista que se muestra
)