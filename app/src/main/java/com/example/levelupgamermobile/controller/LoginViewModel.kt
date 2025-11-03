package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // (1) Obtenemos la instancia de nuestro repositorio
    private val authRepository = AuthRepository

    // (2) El Estado (STATE)
    // "_uiState" es el estado INTERNO y PRIVADO.
    private val _uiState = MutableStateFlow(LoginUiState())
    // "uiState" es el estado PÚBLICO que la UI observará.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // (3) Acciones de la UI (cuando el usuario escribe)
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(pass: String) {
        _uiState.update { it.copy(pass = pass, error = null) }
    }

    // (4) La Acción de Login
    fun doLogin() {
        // No hacer nada si ya estamos cargando
        if (_uiState.value.isLoading) return

        // Ponemos el estado de "Cargando"
        _uiState.update { it.copy(isLoading = true, error = null) }

        // Lanzamos la corrutina para llamar al repositorio en un hilo secundario
        viewModelScope.launch {
            val result = authRepository.login(
                email = _uiState.value.email,
                password = _uiState.value.pass
            )

            // (5) Manejamos el resultado de la llamada
            when (result) {
                is LoginResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true // ¡Éxito!
                        )
                    }
                }
                is LoginResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message // Mostramos el error
                        )
                    }
                }
            }
        }
    }
}