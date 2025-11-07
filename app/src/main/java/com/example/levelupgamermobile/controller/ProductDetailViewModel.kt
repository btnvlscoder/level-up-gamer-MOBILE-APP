package com.example.levelupgamermobile.controller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.CartRepository
import com.example.levelupgamermobile.data.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * gestiona la logica y el estado de la pantalla de detalle de producto.
 *
 * esta clase se encarga de:
 * - recibir el 'productid' desde los argumentos de navegacion.
 * - cargar el producto especifico desde ProductRepository.
 * - cargar las resenas (reviews) para ese producto.
 * - calcular la calificacion promedio.
 * - exponer el ProductDetailUiState combinado a la vista.
 * - manejar las acciones de "agregar al carrito" y "anadir resena".
 */
class ProductDetailViewModel(
    // savedstatehandle nos permite leer los argumentos pasados
    // a esta pantalla a traves del grafo de navegacion.
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // obtiene las instancias singleton de los repositorios.
    private val productRepository = ProductRepository
    private val cartRepository = CartRepository
    private val authRepository = AuthRepository

    // obtenemos el 'productid' desde la ruta de navegacion.
    // checknotnull asegura que la app falle rapido si
    // llegamos aqui sin un id de producto.
    private val productId: String = checkNotNull(savedStateHandle["productId"])

    // stateflow interno para gestionar el estado del *producto* en si.
    // esto nos permite cargarlo una vez y luego combinarlo con
    // otros flujos (como las resenas).
    private val _producto = MutableStateFlow(ProductDetailUiState())

    /**
     * expone el uistate combinado a la vista (view).
     *
     * usamos 'combine' para fusionar el flujo del producto (_producto)
     * con el flujo de las resenas (getreviewsforproduct).
     * si cualquiera de los dos cambia, este bloque se re-ejecuta
     * para calcular el nuevo promedio y emitir un estado actualizado.
     */
    val uiState: StateFlow<ProductDetailUiState> = combine(
        _producto,
        productRepository.getReviewsForProduct(productId)
    ) { productoState, reviews ->

        // logica de negocio: calcula el promedio de calificacion.
        val avgRating = if (reviews.isNotEmpty()) {
            reviews.map { it.calificacion }.average().toFloat()
        } else {
            0f // evita division por cero
        }

        // emite el nuevo estado combinado.
        productoState.copy(
            reviews = reviews,
            averageRating = avgRating
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _producto.value
        )

    // un sharedflow se usa para enviar "eventos" de un solo uso a la ui,
    // como los mensajes de snackbar.
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    /**
     * el bloque 'init' se ejecuta una sola vez cuando
     * el viewmodel es creado.
     */
    init {
        // lanzamos una corrutina para cargar los datos del producto.
        viewModelScope.launch {
            val producto = productRepository.getProductoPorCodigo(productId)
            // actualizamos el flujo '_producto', lo que disparara
            // el 'combine' de arriba.
            _producto.update {
                it.copy(
                    isLoading = false,
                    product = producto,
                    error = if (producto == null) "producto no encontrado" else null
                )
            }
        }
    }

    /**
     * delega la accion de agregar al carrito al repositorio.
     */
    fun addToCart() {
        val producto = _producto.value.product
        if (producto != null) {
            cartRepository.addItem(producto)
        }
    }

    /**
     * gestiona la logica para anadir una nueva resena.
     * comprueba si el usuario ya ha opinado antes de guardar.
     */
    fun addReview(calificacion: Int, comentario: String) {
        viewModelScope.launch {
            // 1. obtenemos los datos del usuario actual desde la sesion.
            val userEmail = authRepository.userEmailFlow.first()
            val userName = authRepository.userNameFlow.first()

            if (userEmail == null || userName == null) {
                _snackbarMessage.emit("error: debes iniciar sesion.")
                return@launch
            }

            // 2. logica de negocio: revisamos si el usuario ya opino.
            // leemos el 'uistate' actual para ver la lista de resenas.
            val yaHaOpinado = uiState.value.reviews.any { it.userEmail == userEmail }

            if (yaHaOpinado) {
                // 3. si ya opino, enviamos un mensaje de error.
                _snackbarMessage.emit("ya has dejado una resena para este producto.")
            } else {
                // 4. si no ha opinado, guardamos la resena.
                productRepository.addReview(
                    productId = productId,
                    userEmail = userEmail,
                    userName = userName,
                    calificacion = calificacion,
                    comentario = comentario
                )
                _snackbarMessage.emit("¡gracias por tu resena!")
            }
        }
    }
}