package com.example.levelupgamermobile.model

/**
 * Representa una sola reseña de un usuario.
 */
data class Resena(
    val id: String, // Un ID único
    val productId: String, // A qué producto pertenece
    val userName: String, // Quién la escribió
    val calificacion: Int, // De 1 a 5
    val comentario: String
)