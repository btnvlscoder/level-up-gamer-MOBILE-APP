package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class VentaResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("fecha") val fecha: String?, 
    @SerializedName("total") val total: Double,
    @SerializedName("usuario") val usuario: UsuarioEntityDTO?,
    @SerializedName("detalles") val detalles: List<DetalleVentaResponseDTO>?
)

data class DetalleVentaResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("codigoProducto") val codigoProducto: String,
    @SerializedName("nombreProducto") val nombreProducto: String,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precio") val precio: Double
)
