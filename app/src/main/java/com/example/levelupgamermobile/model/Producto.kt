package com.example.levelupgamermobile.model

data class Producto(
    val codigo: String,
    val categoria: String,
    val marca: String,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenes: List<Int> // Usaremos IDs de R.drawable en lugar de URLs de "img/..."
)