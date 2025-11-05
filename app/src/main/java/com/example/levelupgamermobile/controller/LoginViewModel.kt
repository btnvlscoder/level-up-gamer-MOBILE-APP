package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * gestiona la logica y el estado de la pantalla de login.
 *
 * esta clase se encarga de:
 * - recibir las acciones del usuario (cambios de texto, clic en boton).
 * - llamar al [authrepository] para procesar el login.
 * - gestionar el estado de la ui (cargando, error, exito) y
 * exponerlo a la vista a traves de [uistate].
 */
class LoginViewModel : ViewModel() {

    // obtiene la instancia singleton del repositorio de autenticacion.
    private val authRepository = AuthRepository

    // _uistate es el stateflow interno y mutable que solo
    // este viewmodel puede modificar.
    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * el uistate publico e inmutable que la vista (view) observa
     * para reaccionar a los cambios de estado.
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * se llama cuando el campo de texto de email cambia en la ui.
     * actualiza el estado y limpia cualquier error anterior.
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    /**
     * se llama cuando el campo de texto de password cambia en la ui.
     * actualiza el estado y limpia cualquier error anterior.
     */
    fun onPasswordChange(pass: String) {
        _uiState.update { it.copy(pass = pass, error = null) }
    }

    /**
     * ejecuta la logica de inicio de sesion.
     * esta funcion es llamada por la vista cuando el usuario
     * presiona el boton de "ingresar".
     */
    fun doLogin() {
        // previene multiples clics si ya se esta procesando una solicitud.
        if (_uiState.value.isLoading) return

        // 1. actualiza el estado a 'cargando' y limpia errores.
        _uiState.update { it.copy(isLoading = true, error = null) }

        // 2. lanza una corrutina en el viewmodelscope.
        // esto asegura que la operacion se cancele si el viewmodel se destruye.
        viewModelScope.launch {
            // 3. llama al repositorio (que usa un hilo secundario)
            val result = authRepository.login(
                email = _uiState.value.email,
                password = _uiState.value.pass
            )

            // 4. maneja el resultado de la operacion de red.
            when (result) {
                is LoginResult.Success -> {
                    // exito: actualiza el estado para disparar la navegacion.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                }
                is LoginResult.Error -> {
                    // error: actualiza el estado para mostrar el mensaje de error.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
}