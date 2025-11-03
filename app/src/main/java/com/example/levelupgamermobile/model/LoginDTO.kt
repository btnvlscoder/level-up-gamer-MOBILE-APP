package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Espejo del LoginDTO.java de Spring Boot.
 * Representa los datos que ENVIAMOS al endpoint /usuario/login.
 *
 * Usamos @SerializedName para asegurarnos de que el nombre
 * en el JSON sea exactamente el que espera el backend.
 */
data class LoginDTO(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)