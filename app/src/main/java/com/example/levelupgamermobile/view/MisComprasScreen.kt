package com.example.levelupgamermobile.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.MisComprasUiState
import com.example.levelupgamermobile.controller.MisComprasViewModel
import com.example.levelupgamermobile.model.Purchase
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla "inteligente" que muestra el historial
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisComprasScreen(
    viewModel: MisComprasViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Compras") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            // (Muestra un indicador de carga si es necesario)
        } else if (uiState.purchases.isEmpty()) {
            // (Mensaje si no hay compras)
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no has realizado ninguna compra.")
            }
        } else {
            // (Muestra la lista de compras)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.purchases, key = { it.id }) { purchase ->
                    PurchaseItem(purchase = purchase)
                }
            }
        }
    }
}

/**
 * Un Composable para mostrar una sola compra (expandible)
 */
@Composable
private fun PurchaseItem(purchase: Purchase) {
    // (1) Estado para saber si la tarjeta está expandida
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // Expande/contrae al hacer clic
    ) {
        Column(Modifier.padding(16.dp)) {
            // (2) Fila de resumen (siempre visible)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        // Formatea la fecha
                        text = "Compra: ${formatDate(purchase.date)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Total: ${formatPrice(purchase.total)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expandir"
                )
            }

            // (3) Detalles (solo visible si está expandido)
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider()
                    Spacer(Modifier.height(16.dp))
                    Text("Items:", style = MaterialTheme.typography.labelLarge)
                    // (4) Lista de items dentro de la compra
                    purchase.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("(${item.cantidad}x) ${item.producto.nombre}")
                            Text(formatPrice(item.producto.precio * item.cantidad))
                        }
                    }
                }
            }
        }
    }
}

// --- Helpers (Funciones Ayudantes) ---

private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale("es", "CL"))
    return sdf.format(Date(timestamp))
}