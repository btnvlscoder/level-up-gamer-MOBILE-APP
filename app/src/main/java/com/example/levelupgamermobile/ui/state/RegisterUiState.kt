package com.example.levelupgamermobile.ui.state

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)
