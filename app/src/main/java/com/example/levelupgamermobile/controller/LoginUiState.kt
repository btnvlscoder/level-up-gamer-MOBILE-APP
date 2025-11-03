package com.example.levelupgamermobile.controller

/**
 * Representa todo lo que la pantalla de Login necesita saber.
 * Es un "molde" para el estado.
 */
data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false, // Para mostrar un CircularProgressIndicator
    val error: String? = null,      // Para mostrar mensajes de error
    val loginSuccess: Boolean = false // Para decirle a la UI que navegue
)