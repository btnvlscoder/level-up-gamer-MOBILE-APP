package com.example.levelupgamermobile.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.LevelUpGamerApp
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.model.CrearVentaRequest
import com.example.levelupgamermobile.model.VentaItemRequest
import com.example.levelupgamermobile.network.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

sealed interface SavePurchaseState {
    object Idle : SavePurchaseState
    object Loading : SavePurchaseState
    object Success : SavePurchaseState
    data class Error(val message: String) : SavePurchaseState
}

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository
    private val sessionManager = LevelUpGamerApp.sessionManager
    private val apiService = RetrofitProvider.apiService

    val uiState: StateFlow<CartUiState> = cartRepository.cartItems
        .map { cartList ->
            val total = cartList.sumOf { it.producto.precio * it.cantidad }
            CartUiState(items = cartList, total = total)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    private val _savePurchaseState = MutableStateFlow<SavePurchaseState>(SavePurchaseState.Idle)
    val savePurchaseState: StateFlow<SavePurchaseState> = _savePurchaseState.asStateFlow()

    fun increaseQuantity(codigo: String) {
        cartRepository.increaseQuantity(codigo)
    }

    fun decreaseQuantity(codigo: String) {
        cartRepository.decreaseQuantity(codigo)
    }

    fun removeItem(codigo: String) {
        cartRepository.removeItem(codigo)
    }

    fun clearCart() {
        cartRepository.clearCart()
    }

    fun savePurchase() {
        if (_savePurchaseState.value is SavePurchaseState.Loading) return

        viewModelScope.launch {
            _savePurchaseState.value = SavePurchaseState.Loading
            Log.d("CartViewModel", "Iniciando proceso de guardado de compra.")

            val itemsToSave = uiState.value.items
            val userId = sessionManager.getUserId()

            if (userId == null) {
                _savePurchaseState.value = SavePurchaseState.Error("No se pudo obtener el usuario. Inicia sesión de nuevo.")
                Log.e("CartViewModel", "ID de usuario es nulo. Abortando compra.")
                return@launch
            }

            if (itemsToSave.isEmpty()) {
                _savePurchaseState.value = SavePurchaseState.Error("El carrito está vacío.")
                return@launch
            }

            val ventaItemsRequest = itemsToSave.map { cartItem ->
                VentaItemRequest(
                    nombreProducto = cartItem.producto.nombre,
                    cantidad = cartItem.cantidad,
                    precio = cartItem.producto.precio
                )
            }

            // CORREGIDO: Construir el objeto que el backend espera
            val crearVentaRequest = CrearVentaRequest(
                userId = userId,
                items = ventaItemsRequest
            )

            Log.d("CartViewModel", "Enviando al backend: $crearVentaRequest")

            try {
                val response = apiService.crearVenta(crearVentaRequest)
                if (response.isSuccessful) {
                    Log.d("CartViewModel", "¡Venta guardada exitosamente en el backend!")
                    clearCart()
                    _savePurchaseState.value = SavePurchaseState.Success
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("CartViewModel", "Falló al guardar la venta. Código: ${response.code()}. Mensaje: $errorBody")
                    _savePurchaseState.value = SavePurchaseState.Error("Error del servidor: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error de red al guardar la venta", e)
                _savePurchaseState.value = SavePurchaseState.Error("Error de conexión. Inténtalo de nuevo.")
            }
        }
    }

    fun resetSavePurchaseState() {
        _savePurchaseState.value = SavePurchaseState.Idle
    }
}
