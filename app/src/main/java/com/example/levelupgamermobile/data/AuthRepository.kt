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

object AuthRepository {

    private val apiService = RetrofitProvider.apiService
    private val sessionManager = LevelUpGamerApp.sessionManager

    // (1) ¡NUEVO! Exponemos TODOS los flujos de datos
    val userEmailFlow: Flow<String?> = sessionManager.userEmailFlow
    val userNameFlow: Flow<String?> = sessionManager.userNameFlow
    val userRutFlow: Flow<String?> = sessionManager.userRutFlow
    val userApellidoPFlow: Flow<String?> = sessionManager.userApellidoPFlow
    val userRolFlow: Flow<String?> = sessionManager.userRolFlow

    /**
     * (2) ¡CAMBIO! Ahora pasamos el objeto 'usuario' completo
     * a sessionManager.saveSession()
     */
    suspend fun login(email: String, password: String): LoginResult {
        val loginDto = LoginDTO(email = email, password = password)
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(loginDto)
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        sessionManager.saveSession(usuario) // ¡AQUÍ!
                        LoginResult.Success(usuario)
                    } else {
                        LoginResult.Error("Respuesta exitosa pero cuerpo vacío.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    LoginResult.Error(errorBody)
                }
            } catch (e: IOException) {
                LoginResult.Error("Error de red: No se pudo conectar al servidor.")
            } catch (e: HttpException) {
                LoginResult.Error("Error HTTP: ${e.message}")
            }
        }
    }

    suspend fun register(usuarioDto: UsuarioDTO): RegisterResult {
        return withContext(Dispatchers.IO) {
            try {
                val response: UsuarioResponse = apiService.registrar(usuarioDto)
                if (response.estado == "OK" && response.usuarioDTO != null) {
                    sessionManager.saveSession(response.usuarioDTO) // ¡AQUÍ!
                    RegisterResult.Success
                } else {
                    RegisterResult.Error(response.mensaje)
                }
            } catch (e: IOException) {
                RegisterResult.Error("Error de red: No se pudo conectar al servidor.")
            } catch (e: HttpException) {
                RegisterResult.Error("Error HTTP: ${e.message}")
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            sessionManager.clearSession()
        }
    }
}

// (Resultados sin cambios)
sealed class LoginResult {
    data class Success(val usuario: UsuarioDTO) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}