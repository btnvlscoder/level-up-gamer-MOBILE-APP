package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.ui.theme.LvlUpGreen

// (1) Creamos datos de ejemplo solo para la maqueta
private data class FakeTicket(
    val id: String,
    val asunto: String,
    val estado: String,
    val colorEstado: Color
)

private val fakeTickets = listOf(
    FakeTicket("T-12345", "Problema con mi compra", "Abierto", LvlUpGreen),
    FakeTicket("T-12342", "Reseña no aparece", "Cerrado", Color.Gray),
    FakeTicket("T-12340", "Consulta sobre garantía", "Cerrado", Color.Gray)
)

/**
 * Pantalla de Maqueta para "Mis Tickets"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisTicketsScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tickets") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        // (2) Mostramos la lista de tickets falsos
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

/**
 * Un Composable para mostrar un solo ticket de maqueta
 */
@Composable
private fun TicketItem(ticket: FakeTicket) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ticket.id,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = ticket.asunto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = ticket.estado.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = ticket.colorEstado,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}