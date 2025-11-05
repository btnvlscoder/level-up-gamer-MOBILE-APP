package com.example.levelupgamermobile.model

/**
 * define la estructura de una compra completada (voucher).
 *
 * esta data class se usa para guardar un registro permanente
 * de una transaccion en el historial del usuario.
 * [SessionManager] la serializa (convierte) a json para
 * guardarla en datastore.
 */
data class Purchase(
    /**
     * un identificador unico (uuid) para esta compra especifica.
     */
    val id: String,

    /**
     * la fecha y hora de la compra, guardada como un 'long'
     * (milisegundos unix, ej. system.currenttimemillis()).
     */
    val date: Long,

    /**
     * la lista de objetos [CartItem] que se incluyeron
     * en esta compra.
     */
    val items: List<CartItem>,

    /**
     * el precio total pagado en esta transaccion.
     */
    val total: Int
)