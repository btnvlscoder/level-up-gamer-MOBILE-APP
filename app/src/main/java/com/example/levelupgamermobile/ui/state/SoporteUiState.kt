package com.example.levelupgamermobile.ui.state

data class SoporteUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userEmail: String = "",
    val userName: String = ""
)
