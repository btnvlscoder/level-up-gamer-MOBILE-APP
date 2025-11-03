package com.example.levelupgamermobile.controller

import com.example.levelupgamermobile.model.Resena

data class MisOpinionesUiState(
    val isLoading: Boolean = true,
    val misResenas: List<Resena> = emptyList()
)