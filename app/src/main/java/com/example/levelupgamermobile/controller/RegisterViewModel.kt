package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.RegisterResult
import com.example.levelupgamermobile.model.UsuarioDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository

    // (1) El Estado (STATE)
    // Usa el "RegisterUiState" correcto
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // (2) Acción de Registro
    // Acepta los parámetros que le envía la Vista
    fun doRegister(
        email: String,
        pass: String,
        confirmPass: String,
        rut: String,
        nombre: String,
        apellidoP: String,
        apellidoM: String
    ) {
        if (_uiState.value.isLoading) return

        // (3) Validación
        if (pass != confirmPass) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }
        if (email.isBlank() || pass.isBlank() || rut.isBlank() || nombre.isBlank()) {
            _uiState.update { it.copy(error = "Rellena todos los campos obligatorios") }
            return
        }

        // (4) Estado de "Cargando"
        _uiState.update { it.copy(isLoading = true, error = null) }

        // Creamos el DTO para enviar al backend
        val newUser = UsuarioDTO(
            email = email,
            password = pass,
            rut = rut,
            nombre = nombre,
            apellidoPaterno = apellidoP,
            apellidoMaterno = apellidoM,
            rol = null // El backend decide el rol
        )

        // (5) Lanzamos la corrutina
        viewModelScope.launch {
            val result = authRepository.register(newUser)

            // (6) Manejamos el resultado
            when (result) {
                is RegisterResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerSuccess = true // ¡Éxito!
                        )
                    }
                }
                is RegisterResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message // Mostramos el error del backend
                        )
                    }
                }
            }
        }
    }
}