package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class VentaItemRequest(
    @SerializedName("nombreProducto")
    val nombreProducto: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio")
    val precio: Int // Cambiado a Int para que coincida con el backend
)
