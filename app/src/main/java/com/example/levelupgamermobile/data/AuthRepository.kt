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
 * Este es el "intermediario" oficial para la autenticación.
 * Es el único que puede hablar con el ApiService y el SessionManager
 * sobre temas de usuarios.
 */
object AuthRepository {

    // (1) Obtenemos las instancias de nuestra "plomería" y "memoria"
    private val apiService = RetrofitProvider.apiService
    private val sessionManager = LevelUpGamerApp.sessionManager

    // (2) Exponemos el "flujo" de email para que el resto de la app
    // sepa si el usuario está logueado o no.
    val userEmailFlow: Flow<String?> = sessionManager.userEmailFlow

    /**
     * Intenta iniciar sesión en el backend.
     */
    suspend fun login(email: String, password: String): LoginResult {
        val loginDto = LoginDTO(email = email, password = password)

        // "withContext(Dispatchers.IO)" mueve esta tarea
        // a un hilo secundario (Input/Output) para no
        // bloquear la interfaz de usuario.
        return withContext(Dispatchers.IO) {
            try {
                // (3) Hacemos la llamada a la API
                val response = apiService.login(loginDto)

                if (response.isSuccessful) {
                    // (4) ¡Éxito! (Código 200-299)
                    val usuario = response.body()
                    if (usuario != null) {
                        // ¡Guardamos la sesión en el disco!
                        sessionManager.saveSession(usuario)
                        LoginResult.Success(usuario)
                    } else {
                        LoginResult.Error("Respuesta exitosa pero cuerpo vacío.")
                    }
                } else {
                    // (5) Error de la API (Código 401, 404, 500, etc.)
                    // Leemos el cuerpo del error (que en tu API es un String)
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    LoginResult.Error(errorBody)
                }
            } catch (e: IOException) {
                // (6) Error de Red (Sin internet, no se puede conectar al 10.0.2.2)
                LoginResult.Error("Error de red: No se pudo conectar al servidor.")
            } catch (e: HttpException) {
                // (7) Error de Retrofit/HTTP
                LoginResult.Error("Error HTTP: ${e.message}")
            }
        }
    }

    /**
     * Intenta registrar un nuevo usuario.
     */
    suspend fun register(usuarioDto: UsuarioDTO): RegisterResult {
        return withContext(Dispatchers.IO) {
            try {
                // Llamamos al endpoint de registro
                val response: UsuarioResponse = apiService.registrar(usuarioDto)

                if (response.estado == "OK") {
                    // Éxito, guardamos la sesión
                    sessionManager.saveSession(response.usuarioDTO!!)
                    RegisterResult.Success
                } else {
                    // Error reportado por el backend (ej. "Email ya existe")
                    RegisterResult.Error(response.mensaje)
                }
            } catch (e: IOException) {
                RegisterResult.Error("Error de red: No se pudo conectar al servidor.")
            } catch (e: HttpException) {
                RegisterResult.Error("Error HTTP: ${e.message}")
            }
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            sessionManager.clearSession()
        }
    }
}

// (8) Clases "selladas" para manejar los resultados
// de forma segura en los ViewModels.

sealed class LoginResult {
    data class Success(val usuario: UsuarioDTO) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}