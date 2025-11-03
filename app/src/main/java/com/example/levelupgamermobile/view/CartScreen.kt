package com.example.levelupgamermobile.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove

import androidx.compose.material3.Button
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.CartUiState
import com.example.levelupgamermobile.controller.CartViewModel
import com.example.levelupgamermobile.model.CartItem
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla "inteligente" del Carrito
 */
@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(),
    onBackPress: () -> Unit,
    onComprarClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    CartScreenContent(
        uiState = uiState,
        onBackPress = onBackPress,
        onComprarClick = onComprarClick,
        onIncrease = { viewModel.increaseQuantity(it) },
        onDecrease = { viewModel.decreaseQuantity(it) },
        onRemove = { viewModel.removeItem(it) }
    )
}

/**
 * Pantalla "tonta" del Carrito
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    uiState: CartUiState,
    onBackPress: () -> Unit,
    onComprarClick: () -> Unit,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:",
                            style = MaterialTheme. typography.titleLarge
                        )
                        Text(
                            formatPrice(uiState.total),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onComprarClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("COMPRAR AHORA")
                    }
                }
            }
        }
    ) { paddingValues ->
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.items, key = { it.producto.codigo }) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = { onIncrease(item.producto.codigo) },
                        onDecrease = { onDecrease(item.producto.codigo) },
                        onRemove = { onRemove(item.producto.codigo) }
                    )
                }
            }
        }
    }
}

/**
 * Composable para UNA fila del carrito
 */
@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item.producto.imagenes.first()),
                    contentDescription = item.producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                    Text(formatPrice(item.producto.precio * item.cantidad))
                }
                IconButton(onClick = onRemove) {
                    // --- CORRECCIÓN AQUÍ ---
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDecrease, enabled = item.cantidad > 1) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Restar"
                    )
                }
                Text(
                    text = "${item.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrease) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Sumar"
                    )
                }
            }
        }
    }
}

// --- FUNCIÓN HELPER (AYUDANTE) ---
private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}