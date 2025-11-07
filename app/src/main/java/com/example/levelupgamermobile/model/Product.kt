package com.example.levelupgamermobile.model

import androidx.annotation.DrawableRes

/**
 * define la estructura de datos para un solo producto.
 * es el "molde" principal para el catalogo.
 *
 * esta data class se usa en [LocalProductDataSource] para definir
 * el catalogo, y es consumida por [ProductListViewModel]
 * y [ProductDetailViewModel].
 *
 * @param codigo el identificador unico del producto (ej. "jm001").
 * @param categoria la categoria a la que pertenece (ej. "juegos de mesa").
 * @param marca el fabricante del producto (ej. "devir").
 * @param nombre el nombre publico del producto (ej. "catan").
 * @param precio el precio de venta como un entero.
 * @param descripcion el texto descriptivo del producto.
 * @param imagenes una lista de enteros (int). cada entero es una
 * referencia a un recurso en 'res/drawable' (ej. r.drawable.catan_1).
 */
data class Product(
    val codigo: String,
    val categoria: String,
    val marca: String,
    val nombre: String,
    val precio: Int,
    val descripcion: String,

    /**
     * una lista de 'int' donde cada numero es un id de recurso
     * (ej. r.drawable.mi_imagen) de la carpeta 'res/drawable'.
     * la anotacion [@DrawableRes] ayuda al ide a verificar esto.
     */
    @DrawableRes
    val imagenes: List<Int>
)