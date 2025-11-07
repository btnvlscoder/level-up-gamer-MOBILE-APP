package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Product
import com.example.levelupgamermobile.model.Review

/**
 * define la estructura de estado para la pantalla "mis opiniones".
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class MisOpinionesUiState(
    /**
     * indica si el viewmodel esta cargando la lista de opiniones.
     * la vista (view) usa esto para mostrar un indicador de carga.
     * el valor por defecto es true.
     */
    val isLoading: Boolean = true,

    /**
     * la lista de opiniones del usuario.
     * usamos un 'pair' (par) para vincular cada resena
     * con la informacion del producto al que pertenece.
     * el 'producto?' puede ser nulo si el producto fue eliminado.
     */
    val opiniones: List<Pair<Review, Product?>> = emptyList()
)