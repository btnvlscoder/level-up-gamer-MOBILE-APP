package com.example.levelupgamermobile.controller

/**
 * (¡RENOMBRADO!)
 * Representa el estado de la pantalla de Información Personal.
 */
data class InfoPersonalUiState( // <-- ¡NOMBRE CAMBIADO!
    val nombreCompleto: String = "Cargando...",
    val email: String = "...",
    val rut: String = "...",
    val isLoading: Boolean = true,
    val logoutComplete: Boolean = false
)