package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.CartItem
import com.example.levelupgamermobile.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * gestiona el estado del carrito de compras en memoria.
 *
 * se implementa como un 'object' (singleton) para asegurar
 * que toda la app (viewmodels, vistas) acceda a la
 * misma y unica instancia del carrito.
 */
object CartRepository {

    // el stateflow interno y mutable que almacena la lista de items.
    // es privado para que solo este repositorio pueda modificarlo.
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    /**
     * el stateflow publico e inmutable.
     * los viewmodels y vistas observan este flujo para reaccionar
     * automaticamente a cualquier cambio en el carrito.
     */
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * anade un producto al carrito.
     *
     * si el producto ya existe en la lista, incrementa su cantidad.
     * si es un producto nuevo, lo anade a la lista con cantidad 1.
     */
    fun addItem(product: Product) {
        // .update es la forma atomica (segura) de modificar el stateflow.
        _cartItems.update { listaActual ->

            // 1. busca si el producto ya existe.
            val itemExistente = listaActual.find { it.product.codigo == product.codigo }

            if (itemExistente != null) {
                // 2a. si existe: mapea la lista y actualiza solo ese item.
                listaActual.map { item ->
                    if (item.product.codigo == product.codigo) {
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        item
                    }
                }
            } else {
                // 2b. si es nuevo: anade el nuevo cartitem a la lista.
                listaActual + CartItem(product = product, cantidad = 1)
            }
        }
    }

    /**
     * elimina un producto del carrito, sin importar la cantidad.
     */
    fun removeItem(codigoProducto: String) {
        _cartItems.update { listaActual ->
            // filtra la lista, excluyendo el item con el codigo coincidente.
            listaActual.filter { it.product.codigo != codigoProducto }
        }
    }

    /**
     * disminuye la cantidad de un item en 1.
     * si la cantidad ya es 1, no hace nada.
     */
    fun decreaseQuantity(codigoProducto: String) {
        _cartItems.update { listaActual ->
            // mapea la lista y solo modifica el item si su cantidad es > 1.
            listaActual.map { item ->
                if (item.product.codigo == codigoProducto && item.cantidad > 1) {
                    item.copy(cantidad = item.cantidad - 1)
                } else {
                    item
                }
            }
        }
    }

    /**
     * aumenta la cantidad de un item en 1.
     */
    fun increaseQuantity(codigoProducto: String) {
        _cartItems.update { listaActual ->
            listaActual.map { item ->
                if (item.product.codigo == codigoProducto) {
                    item.copy(cantidad = item.cantidad + 1)
                } else {
                    item
                }
            }
        }
    }

    /**
     * vacia el carrito por completo.
     * esto se llama despues de que se completa una compra (voucher).
     */
    fun clearCart() {
        _cartItems.update { emptyList() }
    }
}