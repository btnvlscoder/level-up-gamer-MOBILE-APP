package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InfoPersonalViewModel : ViewModel() {

    private val authRepository = AuthRepository

    // (1) Flujos (sin _rutFlow)
    private val _emailFlow = authRepository.userEmailFlow
    private val _nombreFlow = authRepository.userNameFlow
    private val _apellidoPFlow = authRepository.userApellidoPFlow

    private val _logoutState = MutableStateFlow(false)

    // (2) Combine (sin rut)
    private val _userDataFlow = combine(
        _emailFlow, _nombreFlow, _apellidoPFlow
    ) { email, nombre, apellidoP ->
        UserData(email, nombre, apellidoP)
    }

    val uiState: StateFlow<InfoPersonalUiState> = combine(
        _userDataFlow,
        _logoutState
    ) { userData, logoutComplete ->

        val nombreCompleto = if (userData.nombre != null && userData.apellidoP != null) {
            "${userData.nombre} ${userData.apellidoP}"
        } else {
            "Cargando..."
        }

        InfoPersonalUiState(
            nombreCompleto = nombreCompleto,
            email = userData.email ?: "...",
            // rut = userData.rut ?: "...", // <-- ELIMINADO
            isLoading = false,
            logoutComplete = logoutComplete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InfoPersonalUiState()
    )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutState.update { true }
        }
    }
}

// (3) Data class privada (sin rut)
private data class UserData(
    val email: String?,
    val nombre: String?,
    val apellidoP: String?
    // val rut: String? // <-- ELIMINADO
)