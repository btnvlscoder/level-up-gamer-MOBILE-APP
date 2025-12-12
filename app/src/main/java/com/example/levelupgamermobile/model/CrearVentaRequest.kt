package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Coincide con la entidad Venta del backend.
 */
data class CrearVentaRequest(
    @SerializedName("usuario")
    val usuario: UsuarioRequest,

    @SerializedName("detalles")
    val detalles: List<VentaItemRequest>
)

data class UsuarioRequest(
    @SerializedName("email")
    val email: String
)
