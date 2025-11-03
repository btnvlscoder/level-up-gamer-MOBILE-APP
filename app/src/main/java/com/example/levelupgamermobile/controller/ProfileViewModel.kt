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

class ProfileViewModel : ViewModel() {

    private val authRepository = AuthRepository

    // Observamos los 3 flujos de datos del repositorio
    private val _emailFlow = authRepository.userEmailFlow
    private val _nombreFlow = authRepository.userNameFlow
    private val _rutFlow = authRepository.userRutFlow

    // Creamos un StateFlow para el evento de logout
    private val _logoutState = MutableStateFlow(false)

    // Usamos "combine" para fusionar los 4 flujos en UN solo "UiState"
    val uiState: StateFlow<ProfileUiState> = combine(
        _emailFlow, _nombreFlow, _rutFlow, _logoutState
    ) { email, nombre, rut, logoutComplete ->
        ProfileUiState(
            email = email ?: "...",
            nombre = nombre ?: "Cargando...",
            rut = rut ?: "...",
            isLoading = false,
            logoutComplete = logoutComplete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    // Acción de Logout
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutState.update { true }
        }
    }
}