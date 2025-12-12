package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la estructura completa del usuario como entidad devuelta por el backend
 * en respuestas como ResenaResponse o VentaResponse (donde se anida el objeto completo).
 */
data class UsuarioEntityDTO(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String?,
    @SerializedName("fechaRegistro") val fechaRegistro: String?,
    @SerializedName("activo") val activo: Boolean?,
    @SerializedName("persona") val persona: PersonaDTO?,
    @SerializedName("rol") val rol: RolDTO?
)
