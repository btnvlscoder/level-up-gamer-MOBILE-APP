package com.example.levelupgamermobile.model

/**
 * Representa un item dentro de nuestro carrito de compras.
 *
 * @param producto El producto en sí (con su precio, nombre, etc.)
 * @param cantidad Cuántas unidades de este producto tenemos.
 */
data class CartItem(
    val producto: Producto,
    val cantidad: Int
)