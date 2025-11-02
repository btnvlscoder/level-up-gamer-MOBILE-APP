package com.example.levelupgamermobile.controller

// Importamos las clases necesarias
import androidx.lifecycle.ViewModel
import com.example.levelupgamermobile.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Este es el "Cerebro" de la pantalla de productos.
 * Hereda de "ViewModel()" para que Android lo gestione.
 */
class ProductListViewModel : ViewModel() {

    // 1. El Repositorio (nuestro intermediario de datos)
    // Hacemos que sea "private" (privado) para que solo
    // este ViewModel pueda hablar con él.
    private val repository = ProductRepository

    // 2. El Estado (STATE)
    // "_uiState" es el estado INTERNO y PRIVADO. Es "Mutable" (puede cambiar).
    // Inicia con un estado vacío.
    private val _uiState = MutableStateFlow(ProductListUiState())

    // "uiState" es el estado PÚBLICO. Es solo de lectura.
    // La pantalla (View) "escuchará" los cambios de este objeto.
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    // 3. El Bloque de Inicialización
    /**
     * "init" es un bloque de código que se ejecuta
     * automáticamente la PRIMERA VEZ que se crea el ViewModel.
     */
    init {
        // En cuanto se cree, mandamos a cargar los productos.
        cargarProductos()
    }

    // 4. Las Funciones (ACCIONES)
    /**
     * Esta función pide los productos al repositorio
     * y actualiza el estado (_uiState).
     */
    private fun cargarProductos() {
        // 1. Pedimos los productos al repositorio
        val productos = repository.getProductos()

        // 2. Actualizamos el estado
        // .update { ... } es la forma segura de cambiar el estado.
        // "it.copy(...)" crea una copia del estado actual,
        // pero cambiando solo los campos que nos interesan.
        _uiState.update { estadoActual ->
            estadoActual.copy(
                isLoading = false, // Ya no estamos cargando
                productos = productos // Le pasamos la lista de productos
            )
        }
    }
}