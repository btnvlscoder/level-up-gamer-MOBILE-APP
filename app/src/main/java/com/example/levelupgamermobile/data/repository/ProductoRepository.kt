package com.example.levelupgamermobile.data.repository

import com.example.levelupgamermobile.data.local.dao.ProductoDao
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.data.remote.ApiService
import com.example.levelupgamermobile.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

// Repositorio central para productos. Gestiona la lógica offline-first.
class ProductoRepository(
    private val productoDao: ProductoDao,
    private val apiService: ApiService
) {

    // Expone los productos directamente desde la base de datos local (Source of Truth)
    val allProducts: Flow<List<ProductoEntity>> = productoDao.obtenerProductos()

    // Sincroniza productos del backend.
    // Estrategia: Network-First para actualizar, pero la UI siempre lee de Local (Room).
    suspend fun actualizarProductos() {
        withContext(Dispatchers.IO) {
            try {
                // 1. Obtener JSON del backend
                val response = apiService.getProducts()
                
                if (response.isSuccessful && response.body() != null) {
                    val productosRemotos = response.body()!!
                    
                    // 2. Mapear DTO -> Entity y asignar imagen local (Adapter Pattern Lógico)
                    val productosEntidades = productosRemotos.map { dto ->
                        ProductoEntity(
                            codigo = dto.codigo,
                            categoria = dto.categoria,
                            marca = dto.marca,
                            nombre = dto.nombre,
                            precio = dto.precio,
                            descripcion = dto.descripcion,
                            // Mapeo manual: Backend ID -> Android Drawable Resource
                            imageRes = getImageResForProduct(dto.codigo)
                        )
                    }

                    // 3. Sobrescribir caché local (Esto dispara la actualización de la UI automáticamente)
                    productoDao.borrarTodos()
                    productoDao.insertarTodos(productosEntidades)
                    Log.d("ProductoRepository", "Sincronización exitosa: ${productosEntidades.size} productos")
                } else {
                    Log.e("ProductoRepository", "Error API: ${response.code()}")
                    fallbackToLocalIfEmpty()
                }
            } catch (e: Exception) {
                Log.e("ProductoRepository", "Error de red", e)
                fallbackToLocalIfEmpty()
            }
        }
    }

    // Inyecta datos de prueba solo si la base de datos está vacía (Modo Offline/Error)
    private suspend fun fallbackToLocalIfEmpty() {
        if (productoDao.obtenerProductosCount() == 0) {
            productoDao.insertarTodos(getLocalProductsFallback())
        }
    }

    fun getProductByCode(codigo: String): Flow<ProductoEntity?> {
        return productoDao.obtenerProducto(codigo)
    }

    // Adaptador: Convierte códigos de producto (String) a Recursos de Imagen (Int)
    fun getImageResForProduct(codigo: String): Int {
        return when (codigo) {
            "CO001" -> R.drawable.playstation_5_1
            "AC002" -> R.drawable.auriculares_gaming_hyperx_1
            "MS001" -> R.drawable.mouse_gamer_logitech_g502_hero_1
            "JM001" -> R.drawable.catan_1
            "JM002" -> R.drawable.carcassonne_1
            "MP001" -> R.drawable.mousepad_razer_goliathus_extended_chroma_1
            "CG001" -> R.drawable.pc_gamer_asus_rog_strix_1
            "SG001" -> R.drawable.silla_gamersecretlab_titan_1
            "AC001" -> R.drawable.controlador_inalambrico_xbox_series_x_1
            "PP001" -> R.drawable.poleras_personalizadas_1
            else -> R.drawable.placeholder_image
        }
    }

    // Datos hardcodeados para desarrollo o fallback total
    fun getLocalProductsFallback(): List<ProductoEntity> {
        return listOf(
            ProductoEntity(
                "CO001", "Consolas", "Sony", "PlayStation 5", 450000.0,
                "La última consola de Sony con gráficos 4K y SSD ultrarrápido.", R.drawable.playstation_5_1
            ),
            ProductoEntity(
                "AC002", "Audio", "HyperX", "Auriculares Cloud II", 80000.0,
                "Sonido envolvente 7.1 para gaming competitivo.", R.drawable.auriculares_gaming_hyperx_1
            )
            // ... (Se puede extender si es necesario)
        )
    }
}
