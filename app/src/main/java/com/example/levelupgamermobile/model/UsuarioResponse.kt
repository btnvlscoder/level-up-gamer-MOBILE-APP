package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * define la estructura de datos para la respuesta del registro.
 * es un espejo del 'usuarioresponse.java' del backend.
 *
 * esta data class representa el cuerpo (body) json que
 * se recibe desde el endpoint '/usuario' (registro).
 *
 * se usa [SerializedName] para asegurar que el nombre de la clave
 * en el json coincida exactamente con el que espera el backend.
 */
data class UsuarioResponse(

    /**
     * el estado de la operacion (ej. "ok" o "error").
     */
    @SerializedName("estado")
    val estado: String,

    /**
     * un mensaje descriptivo del resultado (ej. "usuario agregado"
     * o "el email ya existe").
     */
    @SerializedName("mensaje")
    val mensaje: String,

    /**
     * el [UsuarioDTO] que se acaba de crear.
     * es 'nullable' (?) porque el backend puede no devolverlo
     * si el registro falla.
     */
    @SerializedName("usuarioDTO")
    val usuarioDTO: UsuarioDTO?
)