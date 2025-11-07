package com.example.levelupgamermobile.controller

/**
 * define la estructura de estado para la pantalla de informacion personal.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class InfoPersonalUiState(
    /**
     * el nombre completo del usuario (nombre + apellido).
     * el valor por defecto es "cargando...".
     */
    val nombreCompleto: String = "Cargando...",

    /**
     * el email del usuario.
     * el valor por defecto es "...".
     */
    val email: String = "...",

    /**
     * indica si el viewmodel esta cargando los datos iniciales.
     * el valor por defecto es true.
     */
    val isLoading: Boolean = true,

    /**
     * un 'trigger' que se pone en true cuando el logout se completa.
     * la vista (view) observa este valor para saber cuando navegar
     * de vuelta a la pantalla de login.
     */
    val logoutComplete: Boolean = false
)