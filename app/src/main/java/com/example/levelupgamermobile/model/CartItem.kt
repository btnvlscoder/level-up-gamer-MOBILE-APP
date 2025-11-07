package com.example.levelupgamermobile.model

/**
 * define la estructura de un item dentro del carrito de compras.
 *
 * esta data class se usa en [CartRepository] para rastrear
 * no solo el producto, sino tambien la cantidad seleccionada.
 *
 * @param product el objeto [product] completo (con precio, nombre, etc.).
 * @param cantidad el numero de unidades de este producto en el carrito.
 */
data class CartItem(
    val product: Product,
    val cantidad: Int
)