package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.LevelUpGamerApp // ¡NUEVO IMPORT!
import com.example.levelupgamermobile.data.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch // ¡NUEVO IMPORT!

/**
 * ViewModel para la pantalla del Carrito.
 */
class CartViewModel : ViewModel() {

    // (1) Obtenemos la instancia de nuestro repositorio Y session manager
    private val cartRepository = CartRepository
    private val sessionManager = LevelUpGamerApp.sessionManager // ¡NUEVO!

    /**
     * (2) "Escuchamos" al StateFlow del repositorio.
     */
    val uiState: StateFlow<CartUiState> = cartRepository.cartItems
        .map { cartList ->
            // (3) Transformamos los datos
            val total = cartList.sumOf { it.producto.precio * it.cantidad }

            CartUiState(
                items = cartList,
                total = total
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    // (5) Acciones del Usuario
    fun increaseQuantity(codigo: String) {
        cartRepository.increaseQuantity(codigo)
    }

    fun decreaseQuantity(codigo: String) {
        cartRepository.decreaseQuantity(codigo)
    }

    fun removeItem(codigo: String) {
        cartRepository.removeItem(codigo)
    }

    /**
     * Llama al repositorio para vaciar el carrito.
     */
    fun clearCart() {
        cartRepository.clearCart()
    }

    /**
     * (6) ¡NUEVA FUNCIÓN!
     * Guarda la compra actual en el historial
     * antes de borrar el carrito.
     */
    fun savePurchase() {
        // Lanzamos una corrutina porque savePurchase es una "suspend fun"
        viewModelScope.launch {
            // Obtenemos los items y el total del estado actual
            val itemsToSave = uiState.value.items
            val totalToSave = uiState.value.total

            if (itemsToSave.isNotEmpty()) {
                sessionManager.savePurchase(itemsToSave, totalToSave)
            }
        }
    }
}