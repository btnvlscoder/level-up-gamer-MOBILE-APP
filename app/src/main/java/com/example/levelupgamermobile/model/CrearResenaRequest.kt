package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class CrearResenaRequest(
    @SerializedName("producto") val producto: ProductoRequest,
    @SerializedName("usuario") val usuario: UsuarioRequest,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String
)

data class ProductoRequest(
    @SerializedName("codigo") val codigo: String
)
