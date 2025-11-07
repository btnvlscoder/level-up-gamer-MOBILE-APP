package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Review
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * gestiona la logica y el estado de la pantalla "mis opiniones".
 *
 * esta clase se encarga de:
 * - observar el email del usuario logueado desde [authrepository].
 * - observar la lista completa de resenas desde [productrepository].
 * - filtrar las resenas para mostrar solo las del usuario actual.
 * - vincular cada resena con su producto correspondiente.
 * - exponer el [misopinionesuistate] final a la vista.
 */
class MisOpinionesViewModel : ViewModel() {

    private val authRepository = AuthRepository
    private val productRepository = ProductRepository

    /**
     * expone el estado (uistate) de las opiniones a la vista (view).
     *
     * usamos 'combine' para fusionar dos flujos (flows) de datos:
     * 1. el email del usuario actual (desde datastore).
     * 2. la lista completa de todas las resenas (desde la memoria).
     *
     * si cualquiera de estos flujos cambia, el bloque 'combine'
     * se re-ejecuta para generar un nuevo estado.
     */
    val uiState: StateFlow<MisOpinionesUiState> = combine(
        authRepository.userEmailFlow,
        productRepository.allReviews
    ) { userEmail: String?, allReviews: List<Review> ->

        // si el usuario no esta logueado (email nulo),
        // devolvemos un estado vacio inmediatamente.
        if (userEmail == null) {
            return@combine MisOpinionesUiState(isLoading = false, opiniones = emptyList())
        }

        // 1. filtramos la lista completa para encontrar solo
        // las resenas que pertenecen a este usuario.
        val misResenas = allReviews.filter { it.userEmail == userEmail }

        // 2. transformamos la lista: para cada resena, buscamos
        // la informacion del producto al que esta asociada.
        val opinionesConProducto = misResenas.map { resena ->
            val producto = productRepository.getProductoPorCodigo(resena.productId)
            // usamos 'pair' para empaquetar la resena y su producto.
            Pair(resena, producto)
        }

        // 3. emitimos el estado final que la ui dibujara.
        MisOpinionesUiState(
            isLoading = false,
            opiniones = opinionesConProducto
        )

    }.stateIn(
        // convierte el flow en un stateflow que la ui puede
        // consumir de forma segura dentro del viewmodelscope.
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MisOpinionesUiState() // estado inicial (cargando)
    )
}