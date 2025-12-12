package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.TicketRepository
import com.example.levelupgamermobile.ui.state.MisTicketsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MisTicketsViewModel(
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MisTicketsUiState(isLoading = true))
    val uiState: StateFlow<MisTicketsUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    // Inicia la carga y monitoreo de tickets
    private fun loadTickets() {
        viewModelScope.launch {
            authRepository.currentUser.collectLatest { user ->
                if (user != null) {
                    // Polling: Actualiza tickets cada 5 segundos mientras la pantalla esté activa
                    while (isActive) {
                        ticketRepository.getTicketsByUser(user.email).collect { tickets ->
                            _uiState.update { 
                                it.copy(isLoading = false, tickets = tickets) 
                            }
                        }
                        delay(5000) 
                    }
                } else {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = "Usuario no autenticado") 
                    }
                }
            }
        }
    }
}
