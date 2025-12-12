package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.local.entity.DetalleVenta
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import com.example.levelupgamermobile.data.repository.CarritoRepository
import com.example.levelupgamermobile.data.repository.VentaRepository
import com.example.levelupgamermobile.ui.state.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val ventaRepository: VentaRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = carritoRepository.carritoItems
        .map { items ->
            val total = items.sumOf { it.precioUnitario * it.cantidad }
            CartUiState(
                items = items,
                total = total
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState(isLoading = true)
        )

    fun increaseQuantity(codigo: String) {
        viewModelScope.launch {
            val item = uiState.value.items.find { it.codigoProducto == codigo }
            if (item != null) {
                carritoRepository.actualizarCantidad(codigo, item.cantidad + 1)
            }
        }
    }

    fun decreaseQuantity(codigo: String) {
        viewModelScope.launch {
            val item = uiState.value.items.find { it.codigoProducto == codigo }
            if (item != null) {
                if (item.cantidad > 1) {
                    carritoRepository.actualizarCantidad(codigo, item.cantidad - 1)
                } else {
                    carritoRepository.eliminarProducto(codigo)
                }
            }
        }
    }

    fun removeItem(codigo: String) {
        viewModelScope.launch {
            carritoRepository.eliminarProducto(codigo)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            carritoRepository.vaciarCarrito()
        }
    }

    fun savePurchase() {
        val currentItems = uiState.value.items
        if (currentItems.isEmpty()) return

        viewModelScope.launch {
            val total = currentItems.sumOf { it.precioUnitario * it.cantidad }
            val detalles = currentItems.map {
                DetalleVenta(
                    codigoProducto = it.codigoProducto,
                    nombreProducto = it.nombreProducto,
                    cantidad = it.cantidad,
                    precio = it.precioUnitario
                )
            }
            
            val nuevaVenta = VentaEntity(
                fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                total = total,
                detalles = detalles
            )
            
            ventaRepository.insertVenta(nuevaVenta)
            // No limpiamos el carrito aquí, lo hacemos explícitamente o después de confirmar
        }
    }
}
