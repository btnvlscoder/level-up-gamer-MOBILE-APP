package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.example.levelupgamermobile.data.AuthRepository // <-- Eliminado
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Producto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    // (1) Repositorios
    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    // AuthRepository ya no es necesario aquí

    // (2) Estado interno para los filtros
    private val _searchQuery = MutableStateFlow("")
    private val _categoryFilter = MutableStateFlow("Todos")

    // (3) Flujo para mensajes de Snackbar
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    // (4) Listas de productos y categorías
    private val _fullProductList = productRepository.getProductos()
    private val _allCategories = listOf("Todos") + _fullProductList
        .map { it.categoria }
        .distinct()
        .sorted()

    // (5) El Estado (STATE) PÚBLICO
    // "combine" ahora solo depende de los filtros
    val uiState: StateFlow<ProductListUiState> = combine(
        _searchQuery,
        _categoryFilter
        // authRepository.userNameFlow fue eliminado
    ) { query, category ->

        // (6) Lógica de Filtro
        val filteredList = _fullProductList.filter { producto ->
            val categoryMatch = (category == "Todos" || producto.categoria == category)
            val queryMatch = (
                    producto.nombre.contains(query, ignoreCase = true) ||
                            producto.marca.contains(query, ignoreCase = true)
                    )
            categoryMatch && queryMatch
        }

        // (7) Devolvemos el nuevo estado (sin userName)
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

    // (8) Acciones de la UI
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryChange(category: String) {
        _categoryFilter.value = category
    }

    fun onAddToCartClick(producto: Producto) {
        cartRepository.addItem(producto)
        viewModelScope.launch {
            _snackbarMessage.emit("${producto.nombre} agregado al carrito")
        }
    }
}