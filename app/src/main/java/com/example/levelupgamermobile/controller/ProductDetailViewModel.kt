package com.example.levelupgamermobile.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.model.Producto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

// ¡Asegúrate de que la data class NO esté aquí!

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    private val authRepository = AuthRepository

    private val productId: String = checkNotNull(savedStateHandle["productId"])
    private val _producto = MutableStateFlow(ProductDetailUiState())

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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _producto.value
        )

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
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

    fun addToCart() {
        val producto = _producto.value.producto
        if (producto != null) {
            cartRepository.addItem(producto)
        }
    }

    fun addReview(calificacion: Int, comentario: String) {
        viewModelScope.launch {
            val userEmail = authRepository.userEmailFlow.first()
            val userName = authRepository.userNameFlow.first()

            if (userEmail == null || userName == null) {
                _snackbarMessage.emit("Error: Debes iniciar sesión.")
                return@launch
            }

            val yaHaOpinado = uiState.value.reviews.any { it.userEmail == userEmail }

            if (yaHaOpinado) {
                _snackbarMessage.emit("Ya has dejado una reseña para este producto.")
            } else {
                productRepository.addReview(
                    productId = productId,
                    userEmail = userEmail,
                    userName = userName,
                    calificacion = calificacion,
                    comentario = comentario
                )
                _snackbarMessage.emit("¡Gracias por tu reseña!")
            }
        }
    }
}