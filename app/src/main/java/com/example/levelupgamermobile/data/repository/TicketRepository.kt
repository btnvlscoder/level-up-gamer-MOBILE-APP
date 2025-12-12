package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.remote.ApiService
import com.example.levelupgamermobile.model.CrearTicketRequest
import com.example.levelupgamermobile.model.TicketDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import android.util.Log

class TicketRepository(private val apiService: ApiService) {

    // Obtiene tickets del usuario desde el backend
    fun getTicketsByUser(email: String): Flow<List<TicketDTO>> = flow {
        try {
            Log.d("TicketRepository", "Solicitando tickets para: $email")
            val response = apiService.getTicketsByUser(email)
            Log.d("TicketRepository", "Respuesta código: ${response.code()}")
            
            if (response.isSuccessful && response.body() != null) {
                val tickets = response.body()!!
                Log.d("TicketRepository", "Tickets recibidos: ${tickets.size}")
                emit(tickets)
            } else {
                Log.e("TicketRepository", "Error al obtener tickets: ${response.errorBody()?.string()}")
                emit(emptyList()) // Retorna lista vacía en error
            }
        } catch (e: Exception) {
            Log.e("TicketRepository", "Excepción al obtener tickets", e)
            emit(emptyList()) // Manejo de excepción seguro
        }
    }

    // Envía un nuevo ticket al backend
    suspend fun createTicket(email: String, asunto: String, mensaje: String): Result<TicketDTO> {
        return try {
            val response = apiService.createTicket(CrearTicketRequest(email, asunto, mensaje))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear ticket: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
