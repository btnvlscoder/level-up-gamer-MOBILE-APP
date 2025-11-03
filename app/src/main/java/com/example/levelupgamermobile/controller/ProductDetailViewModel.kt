package com.example.levelupgamermobile.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.data.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    // (1) SavedStateHandle
    // Este objeto mágico nos lo da Android
    // y sirve para leer los argumentos de navegación
    // (en nuestro caso, el "productId" de la ruta).
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = ProductRepository
    private val cartRepository = CartRepository
    // (2) El Estado (igual que en el ViewModel de lista)
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    // (3) El ID del producto
    // Leemos el argumento "productId" de la ruta.
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    /**
     * (4) El bloque "init"
     * Se ejecuta en cuanto se crea el ViewModel
     */
    init     {
        // Lanzamos una corrutina (proceso en 2do plano)
        // para ir a buscar el producto.
        viewModelScope.launch {
            // Le pedimos al repo que busque el producto por su código
            val producto = repository.getProductoPorCodigo(productId)

            // Actualizamos el estado
            _uiState.update {
                it.copy(
                    isLoading = false,
                    producto = producto,
                    // Si el producto es null, ponemos un error
                    error = if (producto == null) "Producto no encontrado" else null
                )
            }
        }
    }

    fun addToCart() {
        // Obtenemos el producto del estado actual
        val producto = _uiState.value.producto

        // Solo si el producto no es null, lo agregamos
        if (producto != null) {
            cartRepository.addItem(producto)
            // (En el futuro, aquí podríamos mostrar un
            // mensaje de "¡Agregado con éxito!")
        }
    }
}