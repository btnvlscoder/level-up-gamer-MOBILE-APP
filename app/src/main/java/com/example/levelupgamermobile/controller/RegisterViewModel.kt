package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.RegisterResult
import com.example.levelupgamermobile.model.UsuarioDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * gestiona la logica y el estado de la pantalla de registro.
 *
 * esta clase se encarga de:
 * - recibir los datos del formulario desde la vista (view).
 * - validar los datos (ej. contrasenas coincidentes).
 * - llamar al AuthRepository para procesar el registro.
 * - exponer el RegisterUiState (cargando, error, exito) a la vista.
 */
class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository

    // _uistate es el stateflow interno y mutable que solo
    // este viewmodel puede modificar.
    private val _uiState = MutableStateFlow(RegisterUiState())

    /**
     * el uistate publico e inmutable que la vista (view) observa
     * para reaccionar a los cambios de estado.
     */
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * ejecuta la logica de registro.
     * esta funcion es llamada por la vista (view) cuando el usuario
     * presiona el boton de "crear cuenta".
     *
     * @param email el email del formulario.
     * @param pass la contrasena del formulario.
     * @param confirmPass la contrasena de confirmacion.
     * @param rut el rut del formulario.
     * @param nombre el nombre del formulario.
     * @param apellidoP el apellido paterno del formulario.
     * @param apellidoM el apellido materno del formulario.
     */
    fun doRegister(
        email: String,
        pass: String,
        confirmPass: String,
        rut: String,
        nombre: String,
        apellidoP: String,
        apellidoM: String
    ) {
        // previene multiples clics si ya se esta procesando una solicitud.
        if (_uiState.value.isLoading) return

        // 1. validacion de logica de negocio local
        if (pass != confirmPass) {
            _uiState.update { it.copy(error = "las contrasenas no coinciden") }
            return
        }
        if (email.isBlank() || pass.isBlank() || rut.isBlank() || nombre.isBlank()) {
            _uiState.update { it.copy(error = "rellena todos los campos obligatorios") }
            return
        }

        // 2. actualiza el estado a 'cargando'
        _uiState.update { it.copy(isLoading = true, error = null) }

        // 3. crea el objeto de transferencia de datos (dto)
        //    que se enviara al backend.
        val newUser = UsuarioDTO(
            email = email,
            password = pass,
            rut = rut,
            nombre = nombre,
            apellidoPaterno = apellidoP,
            apellidoMaterno = apellidoM,
            rol = null // el backend es responsable de asignar el rol
        )

        // 4. lanza una corrutina para la operacion de red
        viewModelScope.launch {
            val result = authRepository.register(newUser)

            // 5. maneja el resultado de la operacion
            when (result) {
                is RegisterResult.Success -> {
                    // exito: actualiza el estado para disparar la navegacion.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerSuccess = true
                        )
                    }
                }
                is RegisterResult.Error -> {
                    // error: actualiza el estado para mostrar el mensaje
                    // (ej. "el email ya esta en uso").
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