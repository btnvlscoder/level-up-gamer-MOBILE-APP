package com.example.levelupgamermobile.data.local.entity

import com.google.gson.annotations.SerializedName

data class DetalleVenta(
    @SerializedName("codigoProducto")
    val codigoProducto: String,

    @SerializedName("nombreProducto")
    val nombreProducto: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio")
    val precio: Double
)
