package com.example.levelupgamermobile.ui.state

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)
