package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Product
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * gestiona la logica y el estado de la pantalla del catalogo de productos.
 *
 * esta clase es responsable de:
 * - cargar la lista completa de productos desde el ProductRepository.
 * - gestionar los estados de los filtros (busqueda y categoria).
 * - exponer un ProductListUiState reactivo UiState que la vista consume.
 * - manejar las acciones del usuario, como agregar al carrito.
 */
class ProductListViewModel : ViewModel() {

    // obtiene las instancias de los repositorios.
    private val productRepository = ProductRepository
    private val cartRepository = CartRepository

    // flujos (flows) internos para gestionar el estado de los filtros.
    private val _searchQuery = MutableStateFlow("")
    private val _categoryFilter = MutableStateFlow("Todos")

    // un sharedflow se usa para enviar "eventos" de un solo uso a la ui,
    // como los mensajes de snackbar, que no deben re-mostrarse
    // si la pantalla rota o se recompone.
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    // cargamos la lista completa de productos y categorias una sola vez.
    // esto es eficiente porque el filtrado se hara en la memoria.
    private val _fullProductList = productRepository.getProductos()
    private val _allCategories = listOf("Todos") + _fullProductList
        .map { it.categoria }
        .distinct()
        .sorted()

    /**
     * expone el uistate combinado a la vista (view).
     *
     * usamos 'combine' para escuchar los dos flujos (flows) de filtros.
     * si cualquiera de los filtros cambia, este bloque se re-ejecuta
     * automaticamente para crear un nuevo estado.
     */
    val uiState: StateFlow<ProductListUiState> = combine(
        _searchQuery,
        _categoryFilter
    ) { query, category ->

        // logica de filtrado
        val filteredList = _fullProductList.filter { producto ->
            // 1. logica de filtrado por categoria.
            val categoryMatch = (category == "Todos" || producto.categoria == category)
            // 2. logica de filtrado por busqueda (nombre o marca).
            val queryMatch = (
                    producto.nombre.contains(query, ignoreCase = true) ||
                            producto.marca.contains(query, ignoreCase = true)
                    )
            // 3. el producto debe cumplir ambas condiciones.
            categoryMatch && queryMatch
        }

        // emite el nuevo estado que la ui (vista) dibujara.
        ProductListUiState(
            isLoading = false,
            searchQuery = query,
            categoryFilter = category,
            allCategories = _allCategories,
            filteredProducts = filteredList
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = ProductListUiState()
    )

    /**
     * actualiza el 'query' de busqueda.
     * es llamado por la vista (view) cuando el usuario escribe.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * actualiza la categoria seleccionada.
     * es llamado por la vista (view) desde el menu desplegable.
     */
    fun onCategoryChange(category: String) {
        _categoryFilter.value = category
    }

    /**
     * anade un producto al carrito y emite un evento de snackbar.
     * es llamado por la vista (view) desde el boton en el 'productitem'.
     */
    fun onAddToCartClick(product: Product) {
        cartRepository.addItem(product)
        viewModelScope.launch {
            _snackbarMessage.emit("${product.nombre} agregado al carrito")
        }
    }
}