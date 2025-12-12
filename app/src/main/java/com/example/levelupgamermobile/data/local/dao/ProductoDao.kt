package com.example.levelupgamermobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE codigo = :codigo")
    fun obtenerProducto(codigo: String): Flow<ProductoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(productos: List<ProductoEntity>)

    @Query("DELETE FROM productos")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun obtenerProductosCount(): Int
}
