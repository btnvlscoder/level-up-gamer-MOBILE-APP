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

import kotlinx.coroutines.flow.combine

class CartViewModel(
    private val carritoRepository: CarritoRepository,
    private val ventaRepository: VentaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = combine(
        carritoRepository.carritoItems,
        authRepository.currentUser
    ) { items, user ->
        val subtotal = items.sumOf { it.precioUnitario * it.cantidad }
        var discount = 0.0
        
        // Aplicar descuento si es DUOC
        if (user != null) {
            val isDuoc = user.email.lowercase().endsWith("@duocuc.cl") || 
                         user.rol.uppercase().contains("DUOC")
            
            if (isDuoc) {
                discount = subtotal * 0.10
            }
        }

        CartUiState(
            items = items,
            subtotal = subtotal,
            discount = discount,
            total = subtotal - discount
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
                val total = result.getOrNull()?.let { 0.0 } ?: currentItems.sumOf { it.precioUnitario * it.cantidad }
                // Recalculamos el total con descuento para el voucher
                val subtotal = currentItems.sumOf { it.precioUnitario * it.cantidad }
                var discount = 0.0
                if (currentUser.email.lowercase().endsWith("@duocuc.cl") || currentUser.rol.uppercase().contains("DUOC")) {
                     discount = subtotal * 0.10
                }
                val finalTotal = subtotal - discount
                
                // Guardar estado para VoucherScreen antes de limpiar
                _lastPurchase.value = CartUiState(items = currentItems, total = finalTotal, subtotal = subtotal, discount = discount)

                // NO insertamos localmente para evitar duplicados. Confiamos en el sync.
                // Si quisieramos optimismo, deberiamos usar el ID retornado por backend si existiera.
                // Como VentaEntity genera ID local y Backend ID remoto, mejor esperar al sync.
                
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
