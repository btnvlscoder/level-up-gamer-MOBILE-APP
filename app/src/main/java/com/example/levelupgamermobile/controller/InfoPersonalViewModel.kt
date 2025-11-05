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

/**
 * gestiona la logica y el estado de la pantalla de "informacion personal".
 *
 * esta clase se encarga de:
 * - observar los flujos (flows) de datos del usuario desde [authrepository].
 * - combinar multiples flujos de datos en un unico [infopersonaluisate].
 * - construir el nombre completo del usuario.
 * - manejar la logica de cierre de sesion (logout).
 */
class InfoPersonalViewModel : ViewModel() {

    private val authRepository = AuthRepository

    // flujos de datos observados desde el repositorio.
    // estos flujos provienen directamente de datastore.
    private val _emailFlow = authRepository.userEmailFlow
    private val _nombreFlow = authRepository.userNameFlow
    private val _apellidoPFlow = authRepository.userApellidoPFlow

    // flujo interno para gestionar el evento de logout.
    // esto actua como un 'trigger' para la navegacion.
    private val _logoutState = MutableStateFlow(false)

    // combinamos los flujos de datos del usuario en un solo objeto 'userdata'.
    // esto simplifica la logica de combinacion principal.
    private val _userDataFlow = combine(
        _emailFlow, _nombreFlow, _apellidoPFlow
    ) { email, nombre, apellidoP ->
        UserData(email, nombre, apellidoP)
    }

    /**
     * expone el estado (uistate) combinado a la vista (view).
     *
     * este flujo combina el '_userdataflow' (los datos) y el '_logoutstate'
     * (el evento de logout) en un unico stateflow. la vista observa
     * este 'uistate' y se redibuja automaticamente ante cualquier cambio.
     */
    val uiState: StateFlow<InfoPersonalUiState> = combine(
        _userDataFlow,
        _logoutState
    ) { userData, logoutComplete ->

        // logica de negocio para formatear el nombre.
        val nombreCompleto = if (userData.nombre != null && userData.apellidoP != null) {
            "${userData.nombre} ${userData.apellidoP}"
        } else {
            "cargando..."
        }

        // emite el nuevo estado que la ui dibujara.
        InfoPersonalUiState(
            nombreCompleto = nombreCompleto,
            email = userData.email ?: "...",
            isLoading = false,
            logoutComplete = logoutComplete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InfoPersonalUiState() // estado inicial por defecto
    )

    /**
     * ejecuta el proceso de cierre de sesion.
     * llama al repositorio para borrar los datos de datastore
     * y actualiza el '_logoutstate' para disparar la navegacion.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutState.update { true }
        }
    }
}

/**
 * una data class privada usada internamente por este viewmodel
 * para agrupar los flujos de datos del usuario antes de
 * combinarlos con otros estados (como el logout).
 */
private data class UserData(
    val email: String?,
    val nombre: String?,
    val apellidoP: String?
)