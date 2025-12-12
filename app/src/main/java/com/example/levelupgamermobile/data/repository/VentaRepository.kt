package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.VentaDao
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import kotlinx.coroutines.flow.Flow

class VentaRepository(private val ventaDao: VentaDao) {
    val allVentas: Flow<List<VentaEntity>> = ventaDao.obtenerVentas()

    suspend fun insertVenta(venta: VentaEntity) {
        ventaDao.insertarVenta(venta)
    }
}
