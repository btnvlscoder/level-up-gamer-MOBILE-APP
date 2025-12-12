package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.data.repository.CarritoRepository
import com.example.levelupgamermobile.data.repository.ProductoRepository
import com.example.levelupgamermobile.ui.state.ProductListUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    // Internal filter state
    private val _searchQuery = MutableStateFlow("")
    private val _categoryFilter = MutableStateFlow("Todos")

    // Snackbar messages
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    // Data source (SSOT)
    private val _productosFlow = productoRepository.allProducts

    // Public UI State
    val uiState: StateFlow<ProductListUiState> = combine(
        _productosFlow,
        _searchQuery,
        _categoryFilter
    ) { productos, query, category ->
        
        // Calculate categories dynamically
        val allCategories = listOf("Todos") + productos
            .map { it.categoria }
            .distinct()
            .sorted()

        // Filter logic
        val filtered = productos.filter { producto ->
            val matchCategory = category == "Todos" || producto.categoria == category
            val matchQuery = producto.nombre.contains(query, ignoreCase = true) ||
                             producto.marca.contains(query, ignoreCase = true)
            matchCategory && matchQuery
        }

        ProductListUiState(
            isLoading = false,
            searchQuery = query,
            categoryFilter = category,
            allCategories = allCategories,
            filteredProducts = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductListUiState(isLoading = true)
    )

    init {
        actualizarDatos()
    }

    fun actualizarDatos() {
        viewModelScope.launch {
            // Trigger API sync
            productoRepository.actualizarProductos()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryChange(category: String) {
        _categoryFilter.value = category
    }

    fun onAddToCartClick(producto: ProductoEntity) {
        viewModelScope.launch {
            carritoRepository.agregarProducto(producto)
            _snackbarMessage.emit("${producto.nombre} agregado al carrito")
        }
    }
}
