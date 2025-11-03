package com.example.levelupgamermobile.controller

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false
)