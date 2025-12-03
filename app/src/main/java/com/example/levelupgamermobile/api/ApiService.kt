package com.example.levelupgamermobile.api

import com.example.levelupgamermobile.model.CrearVentaRequest
import com.example.levelupgamermobile.model.LoginDTO
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.model.UsuarioResponse
import retrofit2.Response // ¡Importante! Usamos Response<> para leer el código de error
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Este es el contrato que define CÓMO hablar con tu backend Spring Boot.
 * Cada función aquí debe coincidir con un @RequestMapping en tu Controller.
 */
interface ApiService {

    /**
     * Coincide con tu @PostMapping("/usuario/login")
     */
    @POST("usuario/login")
    suspend fun login(@Body loginDto: LoginDTO): Response<UsuarioDTO>

    /**
     * Coincide con tu @PostMapping("/usuario")
     * (El de registro)
     */
    @POST("usuario")
    suspend fun registrar(@Body usuarioDto: UsuarioDTO): UsuarioResponse

    /**
     * Coincide con tu @PostMapping("/ventas/completa")
     */
    @POST("ventas/completa") // <-- CORREGIDO
    suspend fun crearVenta(@Body crearVentaRequest: CrearVentaRequest): Response<Unit>

}