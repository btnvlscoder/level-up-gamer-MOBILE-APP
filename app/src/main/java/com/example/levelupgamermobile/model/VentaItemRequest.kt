package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class VentaItemRequest(
    @SerializedName("codigoProducto")
    val codigoProducto: String,

    @SerializedName("nombreProducto")
    val nombreProducto: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio")
    val precio: Int
)
