package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class PersonaDTO(
    @SerializedName("rut") val rut: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String?
)
