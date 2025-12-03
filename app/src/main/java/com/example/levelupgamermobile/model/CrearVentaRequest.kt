package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Coincide con el CrearVentaRequestDTO del backend.
 * Solo contiene los datos que el backend necesita para procesar la venta.
 */
data class CrearVentaRequest(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("items")
    val items: List<VentaItemRequest>
)
