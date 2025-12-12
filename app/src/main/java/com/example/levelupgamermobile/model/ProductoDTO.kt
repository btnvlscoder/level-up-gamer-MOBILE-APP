package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class ProductoDTO(
    @SerializedName("codigo")
    val codigo: String,

    @SerializedName("categoria")
    val categoria: String,

    @SerializedName("marca")
    val marca: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("descripcion")
    val descripcion: String
)
