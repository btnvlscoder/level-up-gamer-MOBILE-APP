package com.example.levelupgamermobile.controller

data class InfoPersonalUiState(
    val nombreCompleto: String = "Cargando...",
    val email: String = "...",
    val isLoading: Boolean = true,
    val logoutComplete: Boolean = false
)