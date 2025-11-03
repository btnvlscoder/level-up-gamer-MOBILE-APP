package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel para la pantalla del Carrito.
 */
class CartViewModel : ViewModel() {

    // (1) Obtenemos la instancia de nuestro repositorio
    private val cartRepository = CartRepository

    // (2) ¡LA MAGIA!
    // Esta es la parte más importante.
    // "Escuchamos" al StateFlow del repositorio.
    val uiState: StateFlow<CartUiState> = cartRepository.cartItems
        .map { cartList ->
            // (3) Transformamos los datos
            // Cada vez que la lista del repo cambie,
            // esta función ".map" se ejecutará.

            // Calculamos el nuevo total
            val total = cartList.sumOf { it.producto.precio * it.cantidad }

            // Creamos y devolvemos el nuevo UiState
            CartUiState(
                items = cartList,
                total = total
            )
        }
        .stateIn(
            // (4) stateIn convierte este "Flow" en un "StateFlow"
            // que la UI puede consumir de forma segura.
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState() // Estado inicial (vacío)
        )

    // (5) Acciones del Usuario
    // Estas funciones simplemente "delegan" el trabajo
    // al repositorio.

    fun increaseQuantity(codigo: String) {
        cartRepository.increaseQuantity(codigo)
    }

    fun decreaseQuantity(codigo: String) {
        cartRepository.decreaseQuantity(codigo)
    }

    fun removeItem(codigo: String) {
        cartRepository.removeItem(codigo)
    }
}