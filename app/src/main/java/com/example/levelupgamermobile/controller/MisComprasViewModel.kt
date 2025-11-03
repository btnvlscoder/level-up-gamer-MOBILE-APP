package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.LevelUpGamerApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MisComprasViewModel : ViewModel() {

    // (1) Obtenemos la instancia de nuestro SessionManager
    private val sessionManager = LevelUpGamerApp.sessionManager

    /**
     * (2) "Escuchamos" al Flow del historial de compras.
     * Cada vez que se guarde una nueva compra,
     * este 'uiState' se actualizará automáticamente.
     */
    val uiState: StateFlow<MisComprasUiState> = sessionManager.purchaseHistoryFlow
        .map { purchaseList ->
            // (3) Mapeamos la lista a nuestro UiState
            MisComprasUiState(
                isLoading = false,
                purchases = purchaseList
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MisComprasUiState() // Estado inicial (cargando)
        )
}