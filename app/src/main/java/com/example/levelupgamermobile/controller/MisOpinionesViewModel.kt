package com.example.levelupgamermobile.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamermobile.data.AuthRepository
import com.example.levelupgamermobile.data.ProductRepository
import com.example.levelupgamermobile.model.Resena
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
aimport kotlinx.coroutines.launch

class MisOpinionesViewModel : ViewModel() {

    private val authRepository = AuthRepository
    private val productRepository = ProductRepository

    // (1) Creamos un StateFlow privado para el estado
    private val _uiState = MutableStateFlow(MisOpinionesUiState())
    val uiState: StateFlow<MisOpinionesUiState> = _uiState.asStateFlow()

    init {
        // (2) "Escuchamos" a los dos repositorios
        viewModelScope.launch {
            combine(
                authRepository.userEmailFlow, // El email del usuario logueado
                productRepository.allReviewsFlow  // El flujo de TODAS las reseñas
            ) { userEmail, allReviews ->
                // (3) Filtramos las reseñas
                val misResenas = if (userEmail != null) {
                    allReviews.filter { it.userEmail == userEmail }
                } else {
                    emptyList()
                }
                // (4) Devolvemos el nuevo estado
                MisOpinionesUiState(isLoading = false, misResenas = misResenas)
            }.collect {
                // (5) Actualizamos nuestro StateFlow
                _uiState.value = it
            }
        }
    }
}