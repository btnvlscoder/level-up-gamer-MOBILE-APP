package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// (1) Molde de datos local (solo para esta maqueta)
private data class Ticket(
    val id: String,
    val asunto: String,
    val estado: String,
    val colorEstado: Color
)

// (2) Lista de datos falsos
private val fakeTickets = listOf(
    Ticket("T-1005", "No me llegan los productos", "Resuelto", Color.Green),
    Ticket("T-1004", "Problema con el pago", "Resuelto", Color.Green),
    Ticket("T-1003", "Contraseña olvidada", "En Progreso", Color.Yellow),
    Ticket("T-1002", "Quiero cancelar mi compra", "Cerrado", Color.Gray)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisTicketsScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tickets de Soporte") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(fakeTickets) { ticket ->
                TicketItem(ticket = ticket)
            }
        }
    }
}

// (3) Composable para una fila de Ticket
@Composable
private fun TicketItem(ticket: Ticket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ticket #${ticket.id}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = ticket.asunto,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Estado",
                    tint = ticket.colorEstado
                )
                Text(
                    text = ticket.estado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ticket.colorEstado,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}