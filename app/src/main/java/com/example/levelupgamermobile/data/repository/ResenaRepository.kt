package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.ResenaDao
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import kotlinx.coroutines.flow.Flow

class ResenaRepository(private val resenaDao: ResenaDao) {

    // Obtiene reseñas asociadas a un producto
    fun getReviewsByProduct(productId: String): Flow<List<ResenaEntity>> {
        return resenaDao.getReviewsByProduct(productId)
    }

    // Obtiene todas las reseñas hechas por un usuario
    fun getReviewsByUser(userEmail: String): Flow<List<ResenaEntity>> {
        return resenaDao.getReviewsByUser(userEmail)
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
