package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class ResenaResponseDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("producto") val producto: ProductoDTO,
    @SerializedName("usuario") val usuario: UsuarioEntityDTO,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("fecha") val fecha: String?
)
