package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.VentaRepository
import com.example.levelupgamermobile.ui.state.MisComprasUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisComprasViewModel(
    private val ventaRepository: VentaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MisComprasUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                ventaRepository.getVentasByUser(user.email)
            } else {
                flowOf(emptyList())
            }
        }
        .map { ventas ->
            MisComprasUiState(
                isLoading = false,
                purchases = ventas
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MisComprasUiState(isLoading = true)
        )

    init {
        // Initial sync handled by LaunchedEffect in UI or simple init here
        // But let's expose a method to force refresh
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    ventaRepository.syncVentas(user.email)
                }
            }
        }
    }
}
