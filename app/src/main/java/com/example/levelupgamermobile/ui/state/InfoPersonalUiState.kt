package com.example.levelupgamermobile.ui.state

data class InfoPersonalUiState(
    val nombreCompleto: String = "Cargando...",
    val email: String = "...",
    val isLoading: Boolean = false,
    val logoutComplete: Boolean = false
)
