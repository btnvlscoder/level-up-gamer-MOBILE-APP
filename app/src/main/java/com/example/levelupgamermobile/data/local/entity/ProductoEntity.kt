package com.example.levelupgamermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey
    @SerializedName("codigo")
    val codigo: String,

    @SerializedName("categoria")
    val categoria: String,

    @SerializedName("marca")
    val marca: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("descripcion")
    val descripcion: String,

    val imageRes: Int
)
