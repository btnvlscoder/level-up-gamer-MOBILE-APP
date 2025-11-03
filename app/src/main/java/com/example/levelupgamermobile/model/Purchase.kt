package com.example.levelupgamermobile.model

/**
 * Representa una sola compra (un "voucher" guardado)
 * en el historial del usuario.
 */
data class Purchase(
    val id: String,
    val date: Long, // Guardaremos la fecha como un número (milisegundos)
    val items: List<CartItem>,
    val total: Int
)