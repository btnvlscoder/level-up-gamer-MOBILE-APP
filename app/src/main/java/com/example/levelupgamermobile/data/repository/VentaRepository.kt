package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.VentaDao
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import com.example.levelupgamermobile.data.remote.ApiService
import com.example.levelupgamermobile.model.CrearVentaRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

import com.example.levelupgamermobile.data.local.entity.DetalleVenta
import com.example.levelupgamermobile.model.VentaResponseDTO

class VentaRepository(
    private val ventaDao: VentaDao,
    private val apiService: ApiService
) {
    fun getVentasByUser(email: String): Flow<List<VentaEntity>> {
        return ventaDao.obtenerVentasPorUsuario(email)
    }

    suspend fun insertVenta(venta: VentaEntity) {
        ventaDao.insertarVenta(venta)
    }

    suspend fun syncVentas(email: String): Result<Unit> {
        return try {
            val response = apiService.getVentasByUser(email)
            if (response.isSuccessful) {
                val ventasRemote = response.body() ?: emptyList()
                ventaDao.eliminarVentasPorUsuario(email)
                ventasRemote.forEach { dto ->
                    val entity = VentaEntity(
                        id = dto.id,
                        fecha = dto.fecha ?: "",
                        total = dto.total,
                        userEmail = email,
                        detalles = dto.detalles?.map { d ->
                            DetalleVenta(
                                codigoProducto = d.codigoProducto,
                                nombreProducto = d.nombreProducto,
                                cantidad = d.cantidad,
                                precio = d.precio
                            )
                        } ?: emptyList()
                    )
                    ventaDao.insertarVenta(entity)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error fetching sales: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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
