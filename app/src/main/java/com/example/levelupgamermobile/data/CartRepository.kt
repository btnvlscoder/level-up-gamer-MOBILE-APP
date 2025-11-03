package com.example.levelupgamermobile.data

import com.example.levelupgamermobile.model.CartItem
import com.example.levelupgamermobile.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Usamos "object" (Singleton) para que solo exista UNA
 * instancia de este repositorio en toda la app.
 * ¡Todos los ViewModels hablarán con este mismo objeto!
 */
object CartRepository {

    // (1) EL "TABLERO DE ANUNCIOS" PRIVADO
    // Es "Mutable" (modificable) porque solo este objeto
    // puede escribir en él.
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    // (2) EL "TABLERO DE ANUNCIOS" PÚBLICO
    // Es solo "StateFlow" (de solo lectura).
    // Las pantallas y ViewModels "observarán" este flow
    // para enterarse de los cambios.
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * Esta es la función principal para agregar un producto.
     * Es la que llamaremos desde la pantalla de detalle.
     */
    fun addItem(producto: Producto) {
        // .update { ... } es la forma segura de modificar el StateFlow
        _cartItems.update { listaActual ->

            // Buscamos si el producto ya existe en el carrito
            val itemExistente = listaActual.find { it.producto.codigo == producto.codigo }

            if (itemExistente != null) {
                // --- Caso 1: El producto YA existe ---
                // Creamos una nueva lista...
                listaActual.map { item ->
                    if (item.producto.codigo == producto.codigo) {
                        // ...y al item existente, le sumamos 1 a la cantidad
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        // ...los demás items los dejamos igual
                        item
                    }
                }
            } else {
                // --- Caso 2: El producto es NUEVO ---
                // Simplemente lo añadimos a la lista con cantidad 1
                listaActual + CartItem(producto = producto, cantidad = 1)
            }
        }
    }

    // --- (Aquí irán las otras funciones) ---

    fun removeItem(codigoProducto: String) {
        _cartItems.update { listaActual ->
            // Filtramos la lista, dejando fuera el producto
            listaActual.filter { it.producto.codigo != codigoProducto }
        }
    }

    fun decreaseQuantity(codigoProducto: String) {
        _cartItems.update { listaActual ->
            listaActual.map { item ->
                // Si encontramos el item y su cantidad es > 1...
                if (item.producto.codigo == codigoProducto && item.cantidad > 1) {
                    // ...le restamos 1
                    item.copy(cantidad = item.cantidad - 1)
                } else {
                    // ...si no, lo dejamos igual
                    item
                }
            }
        }
    }

    fun increaseQuantity(codigoProducto: String) {
        // Es la misma lógica que "addItem" cuando el producto ya existe
        _cartItems.update { listaActual ->
            listaActual.map { item ->
                if (item.producto.codigo == codigoProducto) {
                    item.copy(cantidad = item.cantidad + 1)
                } else {
                    item
                }
            }
        }
    }
}