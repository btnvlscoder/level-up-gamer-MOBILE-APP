package com.example.levelupgamermobile.controller

/**
 * define la estructura de estado para la pantalla de registro.
 * esta data class es el "molde" que el viewmodel usa
 * para exponer los datos a la ui (vista).
 */
data class RegisterUiState(
    /**
     * indica si una operacion de red (registro) esta en curso.
     * la vista (view) usa esto para mostrar un indicador de carga.
     */
    val isLoading: Boolean = false,

    /**
     * contiene un mensaje de error si la operacion de registro falla.
     * si es 'null', no se muestra ningun error.
     */
    val error: String? = null,

    /**
     * un 'trigger' que se pone en true cuando el registro es exitoso.
     * la vista (view) observa este valor para saber cuando navegar
     * a la pantalla principal (home).
     */
    val registerSuccess: Boolean = false
)