package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.LevelUpGamerApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * gestiona la logica y el estado de la pantalla "mis compras".
 *
 * esta clase se encarga de:
 * - observar el flujo (flow) del historial de compras desde el [sessionmanager].
 * - transformar la lista de compras guardada en un [miscomprasuistate]
 * que la vista (view) pueda consumir.
 */
class MisComprasViewModel : ViewModel() {

    // obtiene la instancia singleton del sessionmanager
    // definida en la clase application.
    private val sessionManager = LevelUpGamerApp.sessionManager

    /**
     * expone el estado (uistate) del historial de compras a la vista (view).
     *
     * observamos 'sessionmanager.purchasehistoryflow'. cada vez que se
     * guarda una nueva compra en datastore, este flujo (flow) emite
     * la lista actualizada.
     *
     * usamos '.map' para transformar esa lista de 'purchase' en
     * nuestro objeto 'miscomprasuistate'.
     */
    val uiState: StateFlow<MisComprasUiState> = sessionManager.purchaseHistoryFlow
        .map { purchaseList ->
            // mapea la lista de datos crudos al estado de la ui
            MisComprasUiState(
                isLoading = false,
                purchases = purchaseList
            )
        }
        .stateIn(
            // convierte el flow en un stateflow que la ui puede
            // consumir de forma segura dentro del viewmodelscope.
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MisComprasUiState() // estado inicial (cargando)
        )
}