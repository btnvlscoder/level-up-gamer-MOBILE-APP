package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Product
import com.example.levelupgamermobile.model.Review

/**
 * define la estructura de estado para la pantalla de detalle de producto.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class ProductDetailUiState(
    /**
     * indica si el viewmodel esta cargando los datos del producto.
     * la vista (view) usa esto para mostrar un indicador de carga.
     * el valor por defecto es true.
     */
    val isLoading: Boolean = true,

    /**
     * el objeto 'producto' completo que se esta mostrando.
     * es 'nullable' (?) porque puede ser nulo mientras 'isloading' es true
     * o si ocurre un error al buscarlo.
     */
    val product: Product? = null,

    /**
     * contiene un mensaje de error si la carga del producto falla.
     * si es 'null', no se muestra ningun error.
     */
    val error: String? = null,

    /**
     * la lista de resenas asociadas a este producto.
     * el valor por defecto es una lista vacia.
     */
    val reviews: List<Review> = emptyList(),

    /**
     * la calificacion promedio (de 1 a 5) calculada
     * a partir de la lista de 'reviews'.
     */
    val averageRating: Float = 0f
)