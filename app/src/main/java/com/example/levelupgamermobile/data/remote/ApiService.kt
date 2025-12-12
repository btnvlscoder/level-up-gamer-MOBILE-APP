package com.example.levelupgamermobile.data.remote

import com.example.levelupgamermobile.model.CrearVentaRequest
import com.example.levelupgamermobile.model.LoginDTO
import com.example.levelupgamermobile.model.UsuarioDTO
import com.example.levelupgamermobile.model.UsuarioResponse
import com.example.levelupgamermobile.model.TicketDTO
import com.example.levelupgamermobile.model.ProductoDTO
import com.example.levelupgamermobile.model.CrearTicketRequest
import com.example.levelupgamermobile.model.VentaResponseDTO
import com.example.levelupgamermobile.model.ResenaResponseDTO
import retrofit2.Response // ¡Importante! Usamos Response<> para leer el código de error
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import com.example.levelupgamermobile.model.CrearResenaRequest

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
    suspend fun registrar(@Body usuarioDto: UsuarioDTO): Response<UsuarioResponse>

    /**
     * Coincide con tu @PostMapping("/ventas")
     */
    @POST("ventas") 
    suspend fun crearVenta(@Body crearVentaRequest: CrearVentaRequest): Response<Unit>

    // --- VENTAS ---
    @GET("ventas/usuario/{email}")
    suspend fun getVentasByUser(@Path("email") email: String): Response<List<VentaResponseDTO>>

    // --- RESENAS ---
    @GET("resenas/usuario/{email}")
    suspend fun getResenasByUser(@Path("email") email: String): Response<List<ResenaResponseDTO>>

    @POST("resenas")
    suspend fun crearResena(@Body request: CrearResenaRequest): Response<ResenaResponseDTO>

    // --- TICKETS ---
    @GET("tickets/usuario/{email}")
    suspend fun getTicketsByUser(@Path("email") email: String): Response<List<TicketDTO>>

    @POST("tickets")
    suspend fun createTicket(@Body request: CrearTicketRequest): Response<TicketDTO>

    // --- PRODUCTOS ---
    @GET("productos")
    suspend fun getProducts(): Response<List<ProductoDTO>>
}