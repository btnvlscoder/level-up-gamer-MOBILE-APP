package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * define la estructura de datos para la peticion de login.
 * es un espejo del 'logindto.java' del backend.
 *
 * esta data class representa el cuerpo (body) json que
 * se envia al endpoint '/usuario/login'.
 *
 * se usa [SerializedName] para asegurar que el nombre de la clave
 * en el json (ej. "email") coincida exactamente con el que
 * espera el backend, independientemente del nombre de la variable en kotlin.
 */
data class LoginDTO(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)