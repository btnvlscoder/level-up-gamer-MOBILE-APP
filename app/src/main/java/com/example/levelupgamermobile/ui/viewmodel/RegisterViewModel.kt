package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.ui.state.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

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

        if (pass != confirmPass) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }
        if (email.isBlank() || pass.isBlank() || rut.isBlank() || nombre.isBlank()) {
            _uiState.update { it.copy(error = "Rellena todos los campos obligatorios") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val newUser = UsuarioDTO(
            email = email,
            password = pass,
            rut = rut,
            nombre = nombre,
            apellidoPaterno = apellidoP,
            apellidoMaterno = apellidoM,
            rol = null
        )

        viewModelScope.launch {
            val result = authRepository.register(newUser)
            
            result.fold(
                onSuccess = {
                     _uiState.update {
                        it.copy(isLoading = false, registerSuccess = true)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }
}
