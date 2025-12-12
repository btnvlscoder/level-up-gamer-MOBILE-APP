package com.example.levelupgamermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "resenas")
data class ResenaEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("productId")
    val productId: String,

    @SerializedName("userEmail")
    val userEmail: String,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("calificacion")
    val calificacion: Int,

    @SerializedName("comentario")
    val comentario: String,
    
    val fecha: Long = System.currentTimeMillis()
)
