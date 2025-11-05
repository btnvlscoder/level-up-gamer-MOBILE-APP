package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.Product
import com.example.levelupgamermobile.model.Review
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

/**
 * gestiona los productos y las resenas.
 *
 * actua como la unica fuente de verdad (single source of truth) para
 * los datos of productos (leidos desde LocalProductDataSource) y
 * las resenas (manejadas en memoria).
 *
 * se implementa como un 'object' (singleton) para que toda la app
 * comparta la misma instancia de datos.
 */
object ProductRepository {

    // --- seccion de productos ---

    /**
     * obtiene la lista completa de productos desde la fuente de datos local.
     */
    fun getProductos(): List<Product> = LocalProductDataSource.products

    /**
     * busca y devuelve un unico producto basado en su codigo.
     */
    fun getProductoPorCodigo(codigo: String): Product? =
        LocalProductDataSource.products.find { it.codigo == codigo }

    // --- seccion de resenas ---

    // un coroutinescope personalizado para este repositorio.
    // se usa para que los flujos (flows) creados con 'statein'
    // puedan vivir mientras la app este activa (ya que un 'object'
    // no tiene 'viewmodelscope').
    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    // el stateflow interno y mutable que actua como nuestra
    // "base de datos" en memoria para las resenas.
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())

    /**
     * expone un flujo (flow) publico de *todas* las resenas en la app.
     * MisOpinionesViewModel observa este flujo.
     */
    val allReviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    /**
     * expone un flujo (flow) publico que contiene solo las resenas
     * para un 'productid' especifico.
     * ProductDetailViewModel observa este flujo.
     */
    fun getReviewsForProduct(productId: String): StateFlow<List<Review>> {
        // usamos 'map' para transformar la lista completa
        // en una lista filtrada.
        return _reviews
            .map { list -> list.filter { it.productId == productId } }
            .stateIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * anade o actualiza una resena del usuario para un producto.
     *
     * implementa la logica de negocio de "1 resena por usuario por producto":
     * si el usuario ya ha opinado sobre este producto, su resena
     * anterior se actualiza con los nuevos datos. si no,
     * se crea una resena nueva.
     */
    fun addReview(
        productId: String,
        userEmail: String,
        userName: String,
        calificacion: Int,
        comentario: String
    ) {
        _reviews.update { listaActual ->
            // 1. busca si el usuario (por email) ya tiene una resena
            //    para este producto especifico.
            val reseñaExistente = listaActual.find {
                it.productId == productId && it.userEmail == userEmail
            }

            // 2. crea el nuevo objeto resena.
            //    usa el id existente si se encontro uno,
            //    o genera un uuid nuevo si es la primera vez.
            val nuevaReview = Review(
                id = reseñaExistente?.id ?: UUID.randomUUID().toString(),
                productId = productId,
                userEmail = userEmail,
                userName = userName,
                calificacion = calificacion,
                comentario = comentario
            )

            if (reseñaExistente != null) {
                // 3a. si existia: mapea la lista y reemplaza el item antiguo.
                listaActual.map {
                    if (it.id == reseñaExistente.id) nuevaReview else it
                }
            } else {
                // 3b. si no existia: anade el nuevo item a la lista.
                listaActual + nuevaReview
            }
        }
    }
}