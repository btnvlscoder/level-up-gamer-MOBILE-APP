package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.ui.state.InfoPersonalUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InfoPersonalViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutComplete = MutableStateFlow(false)

    val uiState: StateFlow<InfoPersonalUiState> = combine(
        authRepository.currentUser,
        _logoutComplete
    ) { currentUser, logoutComplete ->
        if (currentUser != null) {
            InfoPersonalUiState(
                nombreCompleto = "${currentUser.nombre} ${currentUser.apellidoPaterno}",
                email = currentUser.email,
                isLoading = false,
                logoutComplete = logoutComplete
            )
        } else {
            InfoPersonalUiState(
                nombreCompleto = "Usuario no identificado",
                email = "",
                isLoading = false,
                logoutComplete = logoutComplete
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InfoPersonalUiState(isLoading = true)
    )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutComplete.update { true }
        }
    }
    
    fun resetLogoutState() {
        _logoutComplete.update { false }
    }
}
