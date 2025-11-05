package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * define la estructura de datos para el usuario.
 * es un espejo del 'usuariodto.java' del backend.
 *
 * esta data class se usa en dos momentos:
 * 1. como los datos que enviamos al registrarnos (en [AuthRepository]).
 * 2. como los datos que recibimos del backend al iniciar sesion
 * (ej. en [LoginResult]).
 *
 * se usa [SerializedName] para asegurar que el nombre de la clave
 * en el json (ej. "email") coincida exactamente con el que
 * espera el backend.
 */
data class UsuarioDTO(

    @SerializedName("email")
    val email: String,

    /**
     * al registrar, enviamos la contrasena.
     * al recibir (ej. en [LoginResult]), el backend envia "**********"
     * por seguridad.
     */
    @SerializedName("password")
    val password: String,

    @SerializedName("rut")
    val rut: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String,

    /**
     * el rol del usuario (ej. "duocuc", "levelupgamer").
     * es 'nullable' (?) porque el backend puede no enviarlo
     * en todas las respuestas (ej. al registrar).
     */
    @SerializedName("rol")
    val rol: String?
)