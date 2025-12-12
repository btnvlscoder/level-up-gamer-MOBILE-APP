package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.UsuarioDao
import com.example.levelupgamermobile.data.local.entity.UsuarioEntity
import com.example.levelupgamermobile.data.remote.ApiService
import com.example.levelupgamermobile.model.LoginDTO
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.model.UsuarioResponse
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao
) {

    // Flujo observable del usuario actual desde la DB local
    val currentUser: Flow<UsuarioEntity?> = usuarioDao.obtenerUsuarioActual()

    // Autentica con el backend y guarda la sesión localmente
    suspend fun login(email: String, pass: String): Result<UsuarioEntity> {
        return try {
            val response = apiService.login(LoginDTO(email = email, password = pass))
            if (response.isSuccessful) {
                val usuarioDto = response.body()
                if (usuarioDto != null) {
                    val entity = usuarioDto.toEntity()
                    usuarioDao.guardarUsuario(entity)
                    Result.success(entity)
                } else {
                    Result.failure(Exception("Respuesta vacía"))
                }
            } else {
                 Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Registra nuevo usuario en backend y guarda sesión
    suspend fun register(usuarioDto: UsuarioDTO): Result<UsuarioEntity> {
        return try {
             val response = apiService.registrar(usuarioDto)
             if (response.isSuccessful && response.body() != null) {
                 val usuarioResp = response.body()!!
                 if (usuarioResp.estado == "OK" && usuarioResp.usuarioDTO != null) {
                     val entity = usuarioResp.usuarioDTO.toEntity()
                     usuarioDao.guardarUsuario(entity)
                     Result.success(entity)
                 } else {
                     Result.failure(Exception(usuarioResp.mensaje))
                 }
             } else {
                 Result.failure(Exception("Error al registrar: ${response.code()}"))
             }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cierra sesión eliminando datos locales
    suspend fun logout() {
        usuarioDao.borrarUsuario()
    }
    
    // Mapper auxiliar: DTO -> Entity
    private fun UsuarioDTO.toEntity(): UsuarioEntity {
        return UsuarioEntity(
            email = this.email,
            id = this.id,
            rut = this.rut,
            nombre = this.nombre,
            apellidoPaterno = this.apellidoPaterno,
            apellidoMaterno = this.apellidoMaterno,
            rol = this.rol ?: "CLIENTE",
            token = null
        )
    }
}
