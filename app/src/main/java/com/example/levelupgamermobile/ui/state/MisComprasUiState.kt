package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.data.local.entity.VentaEntity

data class MisComprasUiState(
    val isLoading: Boolean = false,
    val purchases: List<VentaEntity> = emptyList()
)
