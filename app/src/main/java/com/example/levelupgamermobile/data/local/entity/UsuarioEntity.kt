package com.example.levelupgamermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    @SerializedName("email")
    val email: String,

    @SerializedName("rut")
    val rut: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: String,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: String,

    @SerializedName("rol")
    val rol: String,
    
    // Token de sesión u otros datos locales
    val token: String? = null
)
