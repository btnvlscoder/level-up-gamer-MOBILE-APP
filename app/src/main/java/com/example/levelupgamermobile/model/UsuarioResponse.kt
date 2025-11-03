package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Espejo del UsuarioResponse.java de Spring Boot.
 * Representa la respuesta que RECIBIMOS al intentar un registro.
 */
data class UsuarioResponse(

    @SerializedName("estado")
    val estado: String,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("usuarioDTO")
    val usuarioDTO: UsuarioDTO? // Es 'nullable' (?) porque puede venir vacío si el registro falla
)