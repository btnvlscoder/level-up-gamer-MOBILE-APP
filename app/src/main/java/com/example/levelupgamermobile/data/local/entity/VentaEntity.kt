package com.example.levelupgamermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.levelupgamermobile.data.local.Converters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ventas")
data class VentaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @SerializedName("fecha")
    val fecha: String, // ISO String

    @SerializedName("total")
    val total: Double,

    @SerializedName("detalles")
    val detalles: List<DetalleVenta>
)
