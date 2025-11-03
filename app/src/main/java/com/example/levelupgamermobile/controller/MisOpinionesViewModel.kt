package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MisOpinionesViewModel : ViewModel() {

    private val authRepository = AuthRepository
    private val productRepository = ProductRepository

    // (1) Combinamos el email del usuario Y la lista de TODAS las reseñas
    val uiState: StateFlow<MisOpinionesUiState> = combine(
        authRepository.userEmailFlow,
        productRepository.allReviews
        // Especificamos los tipos explícitamente para ayudar al compilador.
    ) { userEmail: String?, allReviews: List<Resena> ->

        if (userEmail == null) {
            // Si no hay usuario, no hay opiniones
            return@combine MisOpinionesUiState(isLoading = false, opiniones = emptyList())
        }

        // (2) Filtramos las reseñas que sean del usuario actual
        val misResenas = allReviews.filter { it.userEmail == userEmail }

        // (3) Para cada reseña, buscamos los datos del producto
        val opinionesConProducto = misResenas.map { resena ->
            val producto = productRepository.getProductoPorCodigo(resena.productId)
            // Creamos un "Par" (Reseña, Producto)
            Pair(resena, producto)
        }

        // (4) Emitimos el estado final
        MisOpinionesUiState(
            isLoading = false,
            opiniones = opinionesConProducto
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MisOpinionesUiState() // Estado inicial (cargando)
    )
}