package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.CarritoRepository
import com.example.levelupgamermobile.data.repository.ProductoRepository
import com.example.levelupgamermobile.data.repository.ResenaRepository
import com.example.levelupgamermobile.ui.state.ProductDetailUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.combine

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository,
    private val resenaRepository: ResenaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    val uiState: StateFlow<ProductDetailUiState> = combine(
        productoRepository.getProductByCode(productId),
        resenaRepository.getReviewsByProduct(productId)
    ) { producto, reviews ->
        ProductDetailUiState(
            producto = producto,
            reviews = reviews,
            isLoading = false,
            errorMessage = if (producto == null) "Producto no encontrado" else null
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductDetailUiState(isLoading = true)
    )

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun addToCart() {
        val producto = uiState.value.producto
        if (producto != null) {
            viewModelScope.launch {
                carritoRepository.agregarProducto(producto)
                _snackbarMessage.emit("${producto.nombre} agregado al carrito")
            }
        }
    }

    fun addReview(calificacion: Int, comentario: String) {
        viewModelScope.launch {
            val user = authRepository.currentUser.firstOrNull()
            if (user == null) {
                _snackbarMessage.emit("Debes iniciar sesión para opinar")
                return@launch
            }

            // Verificar si el usuario ya hizo una reseña para este producto
            val existingReview = resenaRepository.getReviewByUserAndProduct(user.email, productId)
            if (existingReview != null) {
                _snackbarMessage.emit("Ya has opinado sobre este producto")
                return@launch
            }
            
            val result = resenaRepository.crearResenaBackend(
                productId = productId,
                userEmail = user.email,
                calificacion = calificacion,
                comentario = comentario
            )

            if (result.isSuccess) {
                _snackbarMessage.emit("¡Opinión guardada!")
                // Refresh reviews list handled by Flow from DAO since repo inserts it on success
            } else {
                _snackbarMessage.emit("Error al guardar opinión: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
