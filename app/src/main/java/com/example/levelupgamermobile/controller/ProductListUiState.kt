package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Product

/**
 * define la estructura de estado para la pantalla del catalogo de productos.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class ProductListUiState(
    /**
     * indica si el viewmodel esta cargando los datos iniciales.
     * la vista (view) usa esto para mostrar un indicador de carga.
     * el valor por defecto es true.
     */
    val isLoading: Boolean = true,

    /**
     * el valor actual del campo de texto de busqueda.
     */
    val searchQuery: String = "",

    /**
     * la categoria actualmente seleccionada en el filtro.
     * el valor por defecto es "todos".
     */
    val categoryFilter: String = "Todos",

    /**
     * la lista completa de categorias disponibles (ej. "consolas", "mouse").
     * se usa para poblar el menu desplegable (dropdown) del filtro.
     */
    val allCategories: List<String> = emptyList(),

    /**
     * la lista de productos ya filtrada (por busqueda y categoria).
     * esta es la lista que la ui (vista) dibuja en la pantalla.
     */
    val filteredProducts: List<Product> = emptyList()
)