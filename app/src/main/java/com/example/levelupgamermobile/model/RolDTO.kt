package com.example.levelupgamermobile.model

import com.google.gson.annotations.SerializedName

data class RolDTO(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String
)
