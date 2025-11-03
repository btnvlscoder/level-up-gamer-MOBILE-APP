package com.example.levelupgamermobile.controller

/**
 * Representa todo lo que la pantalla de Registro necesita saber.
 * (Versión simplificada: la Vista maneja los campos de texto).
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false
)