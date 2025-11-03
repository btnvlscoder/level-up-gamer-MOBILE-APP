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

class InfoPersonalViewModel : ViewModel() { // <-- ¡NOMBRE CAMBIADO!

    private val authRepository = AuthRepository

    // (1) Flujos de datos
    private val _emailFlow = authRepository.userEmailFlow
    private val _nombreFlow = authRepository.userNameFlow
    private val _apellidoPFlow = authRepository.userApellidoPFlow
    private val _rutFlow = authRepository.userRutFlow

    private val _logoutState = MutableStateFlow(false)

    // (2) Combinamos los 4 flujos de datos
    private val _userDataFlow = combine(
        _emailFlow, _nombreFlow, _apellidoPFlow, _rutFlow
    ) { email, nombre, apellidoP, rut ->
        UserData(email, nombre, apellidoP, rut)
    }

    /**
     * (3) Combinamos los datos del usuario + estado de logout
     */
    val uiState: StateFlow<InfoPersonalUiState> = combine( // <-- ¡UiState CAMBIADO!
        _userDataFlow,
        _logoutState
    ) { userData, logoutComplete ->

        val nombreCompleto = if (userData.nombre != null && userData.apellidoP != null) {
            "${userData.nombre} ${userData.apellidoP}"
        } else {
            "Cargando..."
        }

        InfoPersonalUiState( // <-- ¡UiState CAMBIADO!
            nombreCompleto = nombreCompleto,
            email = userData.email ?: "...",
            rut = userData.rut ?: "...",
            isLoading = false,
            logoutComplete = logoutComplete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InfoPersonalUiState() // <-- ¡UiState CAMBIADO!
    )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutState.update { true }
        }
    }
}

/**
 * Data class privada para la combinación
 */
private data class UserData(
    val email: String?,
    val nombre: String?,
    val apellidoP: String?,
    val rut: String?
)