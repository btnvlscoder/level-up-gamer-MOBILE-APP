package com.example.levelupgamermobile.controller

// Importamos el molde del producto
import com.example.levelupgamermobile.model.Producto

/**
 * Esta data class representa el "estado" completo de tu
 * pantalla de lista de productos.
 */
data class ProductListUiState(
    /**
     * ¿Estamos cargando los productos?
     * (Por ahora no lo usaremos, pero es vital para el login)
     */
    val isLoading: Boolean = false,

    /**
     * La lista de productos a mostrar.
     * Por defecto, es una lista vacía.
     */
    val productos: List<Producto> = emptyList()
)