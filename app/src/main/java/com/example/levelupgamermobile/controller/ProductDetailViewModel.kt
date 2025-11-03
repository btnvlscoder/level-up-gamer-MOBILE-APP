package com.example.levelupgamermobile.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.model.Producto // ¡Importa Producto!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    private val authRepository = AuthRepository

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    // Estado interno para el producto
    private val _producto = MutableStateFlow(ProductDetailUiState())

    // El UiState combinado (producto + reseñas)
    val uiState: StateFlow<ProductDetailUiState> = combine(
        _producto,
        productRepository.getReviewsForProduct(productId)
    ) { productoState, reviews ->

        val avgRating = if (reviews.isNotEmpty()) {
            reviews.map { it.calificacion }.average().toFloat()
        } else {
            0f
        }

        productoState.copy(
            reviews = reviews,
            averageRating = avgRating
        )
    }
        // ¡CORREGIDO! Usamos stateIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _producto.value
        )

    init {
        // Carga el producto
        viewModelScope.launch {
            val producto = productRepository.getProductoPorCodigo(productId)
            _producto.update {
                it.copy(
                    isLoading = false,
                    producto = producto,
                    error = if (producto == null) "Producto no encontrado" else null
                )
            }
        }
    }

    // Acción de Agregar al Carrito
    fun addToCart() {
        val producto = _producto.value.producto
        if (producto != null) {
            cartRepository.addItem(producto)
        }
    }

    // Acción de Añadir una reseña
    fun addReview(calificacion: Int, comentario: String) {
        viewModelScope.launch {
            // Obtiene el nombre y email del usuario logueado
            val userName = authRepository.userNameFlow.first() ?: "Anónimo"
            val userEmail = authRepository.userEmailFlow.first() ?: "anon@anon.com"

            productRepository.addReview(
                productId = productId,
                userEmail = userEmail,
                userName = userName,
                calificacion = calificacion,
                comentario = comentario
            )
        }
    }
}