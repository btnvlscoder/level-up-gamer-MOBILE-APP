package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.VentaDao
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import com.example.levelupgamermobile.data.remote.ApiService
import com.example.levelupgamermobile.model.CrearVentaRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class VentaRepository(
    private val ventaDao: VentaDao,
    private val apiService: ApiService
) {
    val allVentas: Flow<List<VentaEntity>> = ventaDao.obtenerVentas()

    suspend fun insertVenta(venta: VentaEntity) {
        ventaDao.insertarVenta(venta)
    }

    suspend fun crearVentaBackend(request: CrearVentaRequest): Result<Unit> {
        return try {
            val response = apiService.crearVenta(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear venta: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
