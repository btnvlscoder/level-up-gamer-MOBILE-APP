package com.example.levelupgamermobile.api

import com.example.levelupgamermobile.model.LoginDTO
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.model.UsuarioResponse
import retrofit2.Response // usamos response<> para leer el codigo de estado http
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * esta interfaz define el "contrato" de nuestra api.
 * cada funcion aqui es un endpoint de retrofit que
 * coincide con un requestmapping en el backend.
 */
interface ApiService {

    /**
     * define el endpoint para el inicio de sesion.
     *
     * usamos response<usuariodto> en lugar de solo usuariodto
     * para poder manejar respuestas de exito (200) y de error (401)
     * de forma controlada, leyendo el codigo de estado (response.issuccessful).
     */
    @POST("usuario/login")
    suspend fun login(@Body loginDto: LoginDTO): Response<UsuarioDTO>

    /**
     * define el endpoint para el registro de un nuevo usuario.
     * coincide con el @postmapping("/usuario") del backend.
     */
    @POST("usuario")
    suspend fun registrar(@Body usuarioDto: UsuarioDTO): UsuarioResponse
}