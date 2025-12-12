package com.example.levelupgamermobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVenta(venta: VentaEntity)

    @Query("SELECT * FROM ventas ORDER BY id DESC")
    fun obtenerVentas(): Flow<List<VentaEntity>>
}
