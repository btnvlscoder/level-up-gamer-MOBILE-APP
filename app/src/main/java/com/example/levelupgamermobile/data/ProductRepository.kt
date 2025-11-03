package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

object ProductRepository {

    // --- Sección de Productos ---
    fun getProductos(): List<Producto> = LocalProductDataSource.productos
    fun getProductoPorCodigo(codigo: String): Producto? =
        LocalProductDataSource.productos.find { it.codigo == codigo }

    // --- Sección de Reseñas ---
    private val repositoryScope = CoroutineScope(Dispatchers.Default)
    private val _reviews = MutableStateFlow<List<Resena>>(emptyList())
    val allReviewsFlow: StateFlow<List<Resena>> = _reviews.asStateFlow()
    /**
     * Devuelve un flujo de reseñas SÓLO para un producto.
     */
    fun getReviewsForProduct(productId: String): StateFlow<List<Resena>> {
        return _reviews
            .map { list -> list.filter { it.productId == productId } }
            // ¡CORREGIDO! Usamos stateIn
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Añade o actualiza una reseña (1 por usuario/producto).
     */
    fun addReview(
        productId: String,
        userEmail: String,
        userName: String,
        calificacion: Int,
        comentario: String
    ) {
        _reviews.update { listaActual ->
            // Busca si el usuario ya dejó una reseña para este producto
            val reseñaExistente = listaActual.find {
                it.productId == productId && it.userEmail == userEmail
            }

            val nuevaResena = Resena(
                id = reseñaExistente?.id ?: UUID.randomUUID().toString(), // Usa ID viejo o crea uno nuevo
                productId = productId,
                userEmail = userEmail,
                userName = userName,
                calificacion = calificacion,
                comentario = comentario
            )

            if (reseñaExistente != null) {
                // Si existe: Reemplaza la reseña antigua
                listaActual.map {
                    if (it.id == reseñaExistente.id) nuevaResena else it
                }
            } else {
                // Si no existe: Añade la nueva reseña
                listaActual + nuevaResena
            }
        }
    }
}