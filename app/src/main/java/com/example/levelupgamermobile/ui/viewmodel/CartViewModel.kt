package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.local.entity.DetalleVenta
import com.example.levelupgamermobile.data.local.entity.VentaEntity
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.CarritoRepository
import com.example.levelupgamermobile.data.repository.VentaRepository
import com.example.levelupgamermobile.model.CrearVentaRequest
import com.example.levelupgamermobile.model.UsuarioRequest
import com.example.levelupgamermobile.model.VentaItemRequest
import com.example.levelupgamermobile.ui.state.CartUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import kotlinx.coroutines.flow.catch

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val ventaRepository: VentaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = carritoRepository.carritoItems
        .map { items ->
            val total = items.sumOf { it.precioUnitario * it.cantidad }
            CartUiState(
                items = items,
                total = total
            )
        }
        .catch { e ->
            emit(CartUiState(errorMessage = "Error al cargar carrito: ${e.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState(isLoading = true)
        )

    private val _purchaseComplete = MutableSharedFlow<Boolean>()
    val purchaseComplete = _purchaseComplete.asSharedFlow()

    private val _lastPurchase = MutableStateFlow<CartUiState?>(null)
    val lastPurchase = _lastPurchase.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

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
            // 1. Obtener usuario actual
            val currentUser = authRepository.currentUser.firstOrNull()
            
            if (currentUser == null) {
                _snackbarMessage.emit("Debes iniciar sesión para comprar")
                return@launch
            }

            // Validación de Email para evitar errores
            if (currentUser.email.isBlank()) {
                 _snackbarMessage.emit("Error de sesión: Email de usuario no válido. Por favor, cierra sesión y vuelve a ingresar.")
                 return@launch
            }

            // 2. Preparar request para backend
            val ventaItems = currentItems.map { 
                VentaItemRequest(
                    codigoProducto = it.codigoProducto,
                    nombreProducto = it.nombreProducto,
                    cantidad = it.cantidad,
                    precio = it.precioUnitario.toInt()
                )
            }
            
            val request = CrearVentaRequest(
                usuario = UsuarioRequest(email = currentUser.email),
                detalles = ventaItems
            )

            // 3. Enviar al backend
            val result = ventaRepository.crearVentaBackend(request)

            if (result.isSuccess) {
                // 4. Guardar copia local (opcional, pero consistente con tu código anterior)
                val total = currentItems.sumOf { it.precioUnitario * it.cantidad }
                
                // Guardar estado para VoucherScreen antes de limpiar
                _lastPurchase.value = CartUiState(items = currentItems, total = total)

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
                
                // 5. Limpiar carrito y notificar
                carritoRepository.vaciarCarrito()
                _purchaseComplete.emit(true)
                _snackbarMessage.emit("¡Compra realizada con éxito!")
            } else {
                _snackbarMessage.emit("Error al realizar la compra: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
