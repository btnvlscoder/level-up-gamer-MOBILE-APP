package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.LevelUpGamerApp
import com.example.levelupgamermobile.data.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * gestiona la logica y el estado de la pantalla del carrito.
 *
 * esta clase se encarga de:
 * - observar el estado del [cartrepository].
 * - transformar la lista de items del carrito en un [cartuistate] publico.
 * - calcular el total del carrito.
 * - exponer funciones para modificar el carrito (agregar, quitar, etc.).
 * - manejar la logica de guardar la compra antes de vaciar el carrito.
 */
class CartViewModel : ViewModel() {

    // obtiene las instancias singleton de los repositorios y el session manager.
    private val cartRepository = CartRepository
    private val sessionManager = LevelUpGamerApp.sessionManager

    /**
     * expone el estado (uistate) del carrito a la vista (view).
     *
     * usamos 'combine' (en este caso, solo 'map') para escuchar
     * cambios en 'cartrepository.cartitems'.
     * cada vez que la lista de items cambia, este bloque se re-ejecuta
     * para calcular el nuevo total y emitir un nuevo uistate.
     */
    val uiState: StateFlow<CartUiState> = cartRepository.cartItems
        .map { cartList ->
            // transforma la lista de datos en un estado de ui
            val total = cartList.sumOf { it.product.precio * it.cantidad }

            CartUiState(
                items = cartList,
                total = total
            )
        }
        .stateIn(
            // convierte el flow en un stateflow que la ui puede
            // consumir de forma segura dentro del viewmodelscope.
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState() // estado inicial mientras se carga
        )

    /**
     * delega la accion de aumentar la cantidad al repositorio.
     */
    fun increaseQuantity(codigo: String) {
        cartRepository.increaseQuantity(codigo)
    }

    /**
     * delega la accion de disminuir la cantidad al repositorio.
     */
    fun decreaseQuantity(codigo: String) {
        cartRepository.decreaseQuantity(codigo)
    }

    /**
     * delega la accion de eliminar un item al repositorio.
     */
    fun removeItem(codigo: String) {
        cartRepository.removeItem(codigo)
    }

    /**
     * delega la accion de vaciar el carrito al repositorio.
     */
    fun clearCart() {
        cartRepository.clearCart()
    }

    /**
     * guarda la compra actual en el historial persistente (datastore)
     * antes de vaciar el carrito.
     *
     * esta funcion es llamada por el 'voucherscreen'
     * al confirmar la compra.
     */
    fun savePurchase() {
        // se lanza una corrutina porque 'savepurchase' es una funcion suspendida.
        viewModelScope.launch {
            val itemsToSave = uiState.value.items
            val totalToSave = uiState.value.total

            if (itemsToSave.isNotEmpty()) {
                sessionManager.savePurchase(itemsToSave, totalToSave)
            }
        }
    }
}