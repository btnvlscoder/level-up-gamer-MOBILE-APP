package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.Producto

object ProductRepository {

    fun getProductos(): List<Producto> {
        return LocalProductDataSource.productos
    }

    fun getProductoPorCodigo(codigo: String): Producto? {
        return LocalProductDataSource.productos.find { producto: Producto ->
            producto.codigo == codigo
        }
    }
}