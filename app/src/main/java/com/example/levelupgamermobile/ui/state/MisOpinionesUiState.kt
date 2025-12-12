package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.data.local.entity.ProductoEntity

data class MisOpinionesUiState(
    val isLoading: Boolean = false,
    val opiniones: List<Pair<Resena, ProductoEntity?>> = emptyList()
)
