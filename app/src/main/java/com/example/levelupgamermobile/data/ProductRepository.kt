package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow // ¡Asegúrate de tener este import!
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

object ProductRepository {

    // --- (SECCIÓN DE PRODUCTOS - SIN CAMBIOS) ---
    fun getProductos(): List<Producto> = LocalProductDataSource.productos

    fun getProductoPorCodigo(codigo: String): Producto? =
        LocalProductDataSource.productos.find { it.codigo == codigo }

    // --- (SECCIÓN DE RESEÑAS) ---
    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    // Nuestra "base de datos" en memoria de reseñas
    private val _reviews = MutableStateFlow<List<Resena>>(emptyList())

    // V--- ¡AQUÍ ESTÁ LA LÍNEA QUE FALTABA! ---V
    /**
     * Expone un flujo de TODAS las reseñas en la app.
     * "MisOpinionesViewModel" escuchará esto.
     */
    val allReviews: StateFlow<List<Resena>> = _reviews.asStateFlow()
    // A--- FIN DE LA LÍNEA ---A

    /**
     * Devuelve un "Flow" (flujo) de reseñas SÓLO para un producto.
     * "ProductDetailViewModel" escuchará esto.
     */
    fun getReviewsForProduct(productId: String): StateFlow<List<Resena>> {
        return _reviews
            .map { list -> list.filter { it.productId == productId } }
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Añade o actualiza una reseña (para 1 usuario por producto).
     */
    fun addReview(
        productId: String,
        userEmail: String,
        userName: String,
        calificacion: Int,
        comentario: String
    ) {
        _reviews.update { listaActual ->
            val reseñaExistente = listaActual.find {
                it.productId == productId && it.userEmail == userEmail
            }

            val nuevaResena = Resena(
                id = reseñaExistente?.id ?: UUID.randomUUID().toString(),
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