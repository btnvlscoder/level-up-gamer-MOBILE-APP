package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
// V--- ¡AÑADE ESTOS IMPORTS! ---V
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
// A--- FIN DE IMPORTS ---A
import java.util.UUID

object ProductRepository {

    // --- (SECCIÓN DE PRODUCTOS - SIN CAMBIOS) ---
    fun getProductos(): List<Producto> {
        return LocalProductDataSource.productos
    }

    fun getProductoPorCodigo(codigo: String): Producto? {
        return LocalProductDataSource.productos.find { producto ->
            producto.codigo == codigo
        }
    }

    // --- (¡NUEVO!) SECCIÓN DE RESEÑAS ---

    // (1) Creamos un Scope para el Repositorio (ya que no es un ViewModel)
    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    private val _reviews = MutableStateFlow<List<Resena>>(emptyList())

    /**
     * Devuelve un "Flow" (flujo) de reseñas SÓLO para un producto.
     */
    fun getReviewsForProduct(productId: String): StateFlow<List<Resena>> {
        return _reviews
            .map { list -> list.filter { it.productId == productId } }
            // V--- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---V
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList() // Valor inicial: lista vacía
            )
        // A--- FIN DE LA CORRECCIÓN ---A
    }

    /**
     * Añade una nueva reseña a nuestra "base de datos" en memoria.
     */
    fun addReview(productId: String, userName: String, calificacion: Int, comentario: String) {
        _reviews.update { listaActual ->
            val nuevaResena = Resena(
                id = UUID.randomUUID().toString(),
                productId = productId,
                userName = userName,
                calificacion = calificacion,
                comentario = comentario
            )
            listaActual + nuevaResena
        }
    }
}