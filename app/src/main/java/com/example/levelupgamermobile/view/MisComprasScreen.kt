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
// --- imports de utilidades (reemplazan las funciones locales) ---
import com.example.levelupgamermobile.utils.formatDate
import com.example.levelupgamermobile.utils.formatPrice

/**
 * el "smart composable" para la pantalla "mis compras".
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [MisComprasViewModel].
 * 2. observar el [MisComprasUiState] (la lista de [Purchase]).
 * 3. manejar el estado de carga (loading) o de lista vacia.
 *
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisComprasScreen(
    viewModel: MisComprasViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    // observa el stateflow del viewmodel.
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("mis compras") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            // (por ahora no se muestra nada mientras carga)
        } else if (uiState.purchases.isEmpty()) {
            // si no hay compras, muestra un mensaje centrado.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("aun no has realizado ninguna compra.")
            }
        } else {
            // si hay compras, muestra la 'lazycolumn' (lista optimizada).
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // itera sobre la lista de compras y dibuja un 'purchaseitem'.
                items(uiState.purchases, key = { it.id }) { purchase ->
                    PurchaseItem(purchase = purchase)
                }
            }
        }
    }
}

/**
 * el "dumb composable" (tonto) que dibuja una unica
 * tarjeta de compra en el historial.
 *
 * esta tarjeta es "expandible" para mostrar los items
 * detallados de la compra.
 *
 * @param purchase el objeto [Purchase] a dibujar.
 */
@Composable
private fun PurchaseItem(purchase: Purchase) {
    // (1) estado local para controlar si la tarjeta esta expandida
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // expande/contrae al hacer clic
    ) {
        Column(Modifier.padding(16.dp)) {
            // (2) fila de resumen (siempre visible)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        // usamos el 'formatdate' de 'utils'
                        text = "compra: ${formatDate(purchase.date)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        // usamos el 'formatprice' de 'utils'
                        "total: ${formatPrice(purchase.total)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "expandir"
                )
            }

            // (3) detalles (solo visible si 'isexpanded' es true)
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider()
                    Spacer(Modifier.height(16.dp))
                    Text("items:", style = MaterialTheme.typography.labelLarge)
                    // (4) lista de items dentro de la compra
                    purchase.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("(${item.cantidad}x) ${item.product.nombre}")
                            Text(formatPrice(item.product.precio * item.cantidad))
                        }
                    }
                }
            }
        }
    }
}
