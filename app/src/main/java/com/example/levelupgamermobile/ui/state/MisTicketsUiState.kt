package com.example.levelupgamermobile.ui.state

import com.example.levelupgamermobile.model.TicketDTO

data class MisTicketsUiState(
    val isLoading: Boolean = false,
    val tickets: List<TicketDTO> = emptyList(),
    val errorMessage: String? = null
)
