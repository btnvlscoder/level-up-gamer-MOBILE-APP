package com.example.levelupgamermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items")
data class CarritoItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val codigoProducto: String,
    val nombreProducto: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val imageRes: Int
)
