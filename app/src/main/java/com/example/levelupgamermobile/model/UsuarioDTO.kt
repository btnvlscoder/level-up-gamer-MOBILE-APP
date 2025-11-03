package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Espejo del UsuarioDTO.java de Spring Boot.
 * Representa los datos del usuario que RECIBIMOS en un login exitoso,
 * y los datos que ENVIAMOS al registrarnos.
 */
data class UsuarioDTO(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String, // Al registrar enviamos esto, al recibir viene "**********"

    @SerializedName("rut")
    val rut: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String,

    @SerializedName("rol")
    val rol: String? // Lo hacemos 'nullable' (?) por si acaso el backend no lo envía
)