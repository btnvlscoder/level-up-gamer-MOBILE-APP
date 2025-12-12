package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.VentaRepository
import com.example.levelupgamermobile.ui.state.MisComprasUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MisComprasViewModel(
    private val ventaRepository: VentaRepository
) : ViewModel() {

    val uiState: StateFlow<MisComprasUiState> = ventaRepository.allVentas
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
}
