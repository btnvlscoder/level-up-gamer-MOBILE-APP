package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.ResenaDao
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import kotlinx.coroutines.flow.Flow

import com.example.levelupgamermobile.data.remote.ApiService

import com.example.levelupgamermobile.model.CrearResenaRequest
import com.example.levelupgamermobile.model.ProductoRequest
import com.example.levelupgamermobile.model.ResenaResponseDTO
import com.example.levelupgamermobile.model.UsuarioRequest

class ResenaRepository(
    private val resenaDao: ResenaDao,
    private val apiService: ApiService
) {

    // Obtiene reseñas asociadas a un producto
    fun getReviewsByProduct(productId: String): Flow<List<ResenaEntity>> {
        return resenaDao.getReviewsByProduct(productId)
    }

    // Obtiene todas las reseñas hechas por un usuario
    fun getReviewsByUser(userEmail: String): Flow<List<ResenaEntity>> {
        return resenaDao.getReviewsByUser(userEmail)
    }

    suspend fun crearResenaBackend(
        productId: String,
        userEmail: String,
        calificacion: Int,
        comentario: String
    ): Result<Unit> {
        return try {
            val request = CrearResenaRequest(
                producto = ProductoRequest(codigo = productId),
                usuario = UsuarioRequest(email = userEmail),
                calificacion = calificacion,
                comentario = comentario
            )
            val response = apiService.crearResena(request)
            if (response.isSuccessful) {
                // Optionally insert locally, or wait for sync
                // For better UX, we might want to insert locally too, but let's rely on sync or manual refresh for now to avoid ID conflicts
                // Actually, response body has the new ID.
                val dto = response.body()
                if (dto != null) {
                    val entity = ResenaEntity(
                        id = dto.id.toString(),
                        productId = dto.producto.codigo,
                        userEmail = dto.usuario.email,
                        userName = dto.usuario.persona?.nombre ?: "Usuario",
                        calificacion = dto.calificacion,
                        comentario = dto.comentario,
                        fecha = System.currentTimeMillis() // Or parse dto.fecha
                    )
                    resenaDao.insertReview(entity)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error creando reseña: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncResenas(email: String): Result<Unit> {
        return try {
            val response = apiService.getResenasByUser(email)
            if (response.isSuccessful) {
                val resenasRemote = response.body() ?: emptyList()
                resenaDao.deleteReviewsByUser(email)
                resenasRemote.forEach { dto ->
                    val fechaMillis = try {
                        if (dto.fecha != null) {
                            java.time.LocalDateTime.parse(dto.fecha)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        } else {
                            System.currentTimeMillis()
                        }
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }

                    val entity = ResenaEntity(
                        id = dto.id.toString(),
                        productId = dto.producto.codigo,
                        userEmail = dto.usuario.email,
                        userName = dto.usuario.persona?.nombre ?: "Usuario",
                        calificacion = dto.calificacion,
                        comentario = dto.comentario,
                        fecha = fechaMillis
                    )
                    resenaDao.insertReview(entity)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error fetching reviews: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Busca una reseña específica de un usuario para un producto
    suspend fun getReviewByUserAndProduct(userEmail: String, productId: String): ResenaEntity? {
        return resenaDao.getReviewByUserAndProduct(userEmail, productId)
    }

    // Guarda o actualiza una reseña localmente
    suspend fun insertReview(resena: ResenaEntity) {
        resenaDao.insertReview(resena)
    }
}
