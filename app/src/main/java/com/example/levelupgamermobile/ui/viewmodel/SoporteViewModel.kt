package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.TicketRepository
import com.example.levelupgamermobile.ui.state.SoporteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoporteViewModel(
    private val authRepository: AuthRepository,
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoporteUiState())
    val uiState: StateFlow<SoporteUiState> = _uiState.asStateFlow()

    init {
        observeUser()
    }

    // Observa al usuario actual para pre-llenar datos en el formulario
    private fun observeUser() {
        viewModelScope.launch {
            authRepository.currentUser.collectLatest { user ->
                _uiState.update { currentState ->
                    currentState.copy(
                        userEmail = user?.email ?: "",
                        userName = if (user != null) {
                            "${user.nombre} ${user.apellidoPaterno} ${user.apellidoMaterno}".trim()
                        } else {
                            ""
                        }
                    )
                }
            }
        }
    }

    // Procesa el envío del ticket al backend
    fun enviarTicket(email: String, asunto: String, mensaje: String) {
        if (email.isBlank()) {
             _uiState.update { it.copy(errorMessage = "El correo es obligatorio") }
             return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            
            val result = ticketRepository.createTicket(email, asunto, mensaje)
            
            if (result.isSuccess) {
                 _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                 _uiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido") }
            }
        }
    }
    
    fun resetState() {
        _uiState.update { it.copy(isSuccess = false, errorMessage = null) }
    }
}
