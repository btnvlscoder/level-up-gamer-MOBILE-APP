package com.example.levelupgamermobile.model

/**
 * Representa una sola reseña de un usuario.
 */
data class Resena(
    val id: String,
    val productId: String,
    val userEmail: String, // ¡NUEVO! El ID único del usuario
    val userName: String,  // ¡NUEVO! El nombre de pila (ej. "Bastián")
    val calificacion: Int,
    val comentario: String
)