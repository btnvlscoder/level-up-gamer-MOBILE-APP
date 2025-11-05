package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.LevelUpGamerApp
import com.example.levelupgamermobile.model.LoginDTO
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.model.UsuarioResponse
import com.example.levelupgamermobile.network.RetrofitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * es el "intermediario" oficial para la autenticacion.
 * actua como la unica fuente de verdad (single source of truth)
 * para la logica de negocio relacionada con los usuarios.
 *
 * es responsable de:
 * - llamar a los endpoints de la api (login, register).
 * - guardar y leer la sesion del usuario desde [sessionmanager].
 * - exponer los datos del usuario (ej. email) al resto de la app.
 * - manejar y parsear los errores de red.
 */
object AuthRepository {

    // obtiene las instancias singleton de la api y el session manager.
    private val apiService = RetrofitProvider.apiService
    private val sessionManager = LevelUpGamerApp.sessionManager

    // expone los flujos (flows) de datos del usuario desde el session manager.
    // los viewmodels observaran estos flujos para reaccionar a cambios
    // en la sesion (ej. login, logout).
    val userEmailFlow: Flow<String?> = sessionManager.userEmailFlow
    val userNameFlow: Flow<String?> = sessionManager.userNameFlow
    val userApellidoPFlow: Flow<String?> = sessionManager.userApellidoPFlow
    val userRolFlow: Flow<String?> = sessionManager.userRolFlow

    /**
     * intenta iniciar sesion contra el backend.
     * maneja tanto los exitos (guardando la sesion) como
     * los multiples tipos de error (api, red).
     */
    suspend fun login(email: String, password: String): LoginResult {
        val loginDto = LoginDTO(email = email, password = password)

        // ejecuta la llamada de red en el hilo de io (entrada/salida)
        // para no bloquear la ui.
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(loginDto)

                if (response.isSuccessful) {
                    // exito (codigo 200-299)
                    val usuario = response.body()
                    if (usuario != null) {
                        // guarda la sesion del usuario en datastore
                        sessionManager.saveSession(usuario)
                        LoginResult.Success(usuario)
                    } else {
                        // exito, pero el backend no envio datos
                        LoginResult.Error("respuesta exitosa pero cuerpo vacio.")
                    }
                } else {
                    // error de api (codigo 4xx o 5xx)
                    // lee el mensaje de error que envia el backend
                    val errorBody = response.errorBody()?.string() ?: "error desconocido"
                    LoginResult.Error(errorBody)
                }
            } catch (e: IOException) {
                // error de red (ej. sin internet, no se puede conectar al servidor)
                LoginResult.Error("error de red: no se pudo conectar al servidor.")
            } catch (e: HttpException) {
                // error http generico (ej. problemas con retrofit)
                LoginResult.Error("error http: ${e.message}")
            }
        }
    }

    /**
     * intenta registrar un nuevo usuario en el backend.
     * si el registro es exitoso, tambien inicia la sesion.
     */
    suspend fun register(usuarioDto: UsuarioDTO): RegisterResult {
        return withContext(Dispatchers.IO) {
            try {
                val response: UsuarioResponse = apiService.registrar(usuarioDto)

                // el backend responde con un json que incluye un campo "estado"
                if (response.estado == "ok" && response.usuarioDTO != null) {
                    // exito: guarda la sesion del nuevo usuario
                    sessionManager.saveSession(response.usuarioDTO)
                    RegisterResult.Success
                } else {
                    // error logico (ej. "el email ya existe")
                    RegisterResult.Error(response.mensaje)
                }
            } catch (e: IOException) {
                // error de red
                RegisterResult.Error("error de red: no se pudo conectar al servidor.")
            } catch (e: HttpException) {
                // error http
                RegisterResult.Error("error http: ${e.message}")
            }
        }
    }

    /**
     * cierra la sesion del usuario actual
     * borrando sus datos de datastore.
     */
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            sessionManager.clearSession()
        }
    }
}

/**
 * representa los posibles resultados de la operacion de login.
 * usar una clase sellada (sealed class) nos permite manejar
 * el exito y el error de forma segura en el viewmodel.
 */
sealed class LoginResult {
    data class Success(val usuario: UsuarioDTO) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

/**
 * representa los posibles resultados de la operacion de registro.
 */
sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}