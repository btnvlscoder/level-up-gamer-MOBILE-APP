package com.example.levelupgamermobile.controller

/**
 * define la estructura de estado para la pantalla de login.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class LoginUiState(
    /**
     * el valor actual del campo de texto email.
     */
    val email: String = "",

    /**
     * el valor actual del campo de texto password.
     */
    val pass: String = "",

    /**
     * indica si una operacion de red (login) esta en curso.
     * la vista usa esto para mostrar un indicador de carga.
     */
    val isLoading: Boolean = false,

    /**
     * contiene un mensaje de error si la operacion de login falla.
     * si es 'null', no se muestra ningun error.
     */
    val error: String? = null,

    /**
     * un 'trigger' que se pone en true cuando el login es exitoso.
     * la vista (view) observa este valor para saber cuando navegar
     * a la pantalla principal (home).
     */
    val loginSuccess: Boolean = false
)