package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.CarritoDao
import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

class CarritoRepository(
    private val carritoDao: CarritoDao
) {
    val carritoItems: Flow<List<CarritoItemEntity>> = carritoDao.obtenerCarrito()

    // Agrega un producto al carrito o actualiza si ya existe (lógica delegada al DAO)
    suspend fun agregarProducto(producto: ProductoEntity, cantidad: Int = 1) {
        val item = CarritoItemEntity(
            codigoProducto = producto.codigo,
            nombreProducto = producto.nombre,
            precioUnitario = producto.precio,
            cantidad = cantidad,
            imageRes = producto.imageRes
        )
        carritoDao.agregarItem(item)
    }

    // Elimina un producto específico del carrito
    suspend fun eliminarProducto(codigoProducto: String) {
        carritoDao.eliminarItem(codigoProducto)
    }
    
    // Elimina todos los items del carrito
    suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }

    // Actualiza la cantidad de un item; si es 0, lo elimina
    suspend fun actualizarCantidad(codigoProducto: String, nuevaCantidad: Int) {
        if (nuevaCantidad > 0) {
            carritoDao.actualizarCantidad(codigoProducto, nuevaCantidad)
        } else {
            carritoDao.eliminarItem(codigoProducto)
        }
    }
}
