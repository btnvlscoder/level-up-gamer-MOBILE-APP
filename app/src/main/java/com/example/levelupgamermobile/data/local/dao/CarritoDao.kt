package com.example.levelupgamermobile.data.local.dao

import androidx.room.*
import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito_items")
    fun obtenerCarrito(): Flow<List<CarritoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarItem(item: CarritoItemEntity)

    @Query("DELETE FROM carrito_items WHERE codigoProducto = :codigoProducto")
    suspend fun eliminarItem(codigoProducto: String)

    @Query("DELETE FROM carrito_items")
    suspend fun vaciarCarrito()
    
    @Query("UPDATE carrito_items SET cantidad = :nuevaCantidad WHERE codigoProducto = :codigoProducto")
    suspend fun actualizarCantidad(codigoProducto: String, nuevaCantidad: Int)
}
