package com.example.levelupgamermobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.repository.AuthRepository
import com.example.levelupgamermobile.data.repository.ProductoRepository
import com.example.levelupgamermobile.data.repository.ResenaRepository
import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.ui.state.MisOpinionesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class MisOpinionesViewModel(
    private val authRepository: AuthRepository,
    private val resenaRepository: ResenaRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    val uiState: StateFlow<MisOpinionesUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                combine(
                    resenaRepository.getReviewsByUser(user.email),
                    productoRepository.allProducts
                ) { resenas, productos ->
                    val opiniones = resenas.map { resenaEntity ->
                        val producto = productos.find { it.codigo == resenaEntity.productId }
                        val resenaModel = Resena(
                            id = resenaEntity.id,
                            productId = resenaEntity.productId,
                            userEmail = resenaEntity.userEmail,
                            userName = resenaEntity.userName,
                            calificacion = resenaEntity.calificacion,
                            comentario = resenaEntity.comentario
                        )
                        resenaModel to producto
                    }
                    MisOpinionesUiState(
                        isLoading = false,
                        opiniones = opiniones
                    )
                }
            } else {
                flowOf(MisOpinionesUiState(isLoading = false))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MisOpinionesUiState(isLoading = true)
        )
}
