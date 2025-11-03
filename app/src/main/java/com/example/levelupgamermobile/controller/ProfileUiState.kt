package com.example.levelupgamermobile.controller

/**
 * Representa el estado de la pantalla de Perfil.
 * Contiene los datos del usuario que estamos mostrando.
 */
data class ProfileUiState(
    val email: String = "",
    val nombre: String = "",
    val rut: String = "",
    val isLoading: Boolean = true,
    val logoutComplete: Boolean = false // Se pone en "true" para navegar al Login
)