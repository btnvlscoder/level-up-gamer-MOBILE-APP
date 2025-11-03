package com.example.levelupgamermobile.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted // Import
import kotlinx.coroutines.flow.stateIn // Import

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // (1) Obtenemos todos los repositorios
    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    private val authRepository = AuthRepository

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    // (2) Estado interno para el producto y el error
    private val _producto = MutableStateFlow(ProductDetailUiState())

    // (3) El UiState se "combina"
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
        // (4) ¡CORREGIDO! Usamos stateIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _producto.value
        )

    init {
        // Carga el producto (esto es igual que antes)
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

    // (6) Acción de Agregar al Carrito (sin cambios)
    fun addToCart() {
        val producto = _producto.value.producto
        if (producto != null) {
            cartRepository.addItem(producto)
        }
    }

    // (7) ¡NUEVA ACCIÓN! Añadir una reseña
    fun addReview(calificacion: Int, comentario: String) {
        viewModelScope.launch {
            val userName = authRepository.userNameFlow.first() ?: "Anónimo"

            productRepository.addReview(
                productId = productId,
                userName = userName,
                calificacion = calificacion,
                comentario = comentario
            )
        }
    }
}