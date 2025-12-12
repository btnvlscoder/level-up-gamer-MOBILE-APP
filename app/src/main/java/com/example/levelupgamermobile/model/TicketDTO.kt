package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class TicketDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("asunto")
    val asunto: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("fechaEnvio")
    val fechaEnvio: String,
    @SerializedName("estado")
    val estado: String
)

data class CrearTicketRequest(
    val email: String,
    val asunto: String,
    val mensaje: String
)
