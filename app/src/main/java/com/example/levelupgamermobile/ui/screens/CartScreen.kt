package com.example.levelupgamermobile.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
// import coil.compose.AsyncImage
import com.example.levelupgamermobile.ui.state.CartUiState
import com.example.levelupgamermobile.ui.viewmodel.CartViewModel
import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity
import com.example.levelupgamermobile.ui.AppViewModelProvider
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla "inteligente" del Carrito
 */
@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackPress: () -> Unit,
    onComprarClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                            formatPrice(uiState.total.toInt()),
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
                items(uiState.items, key = { it.codigoProducto }) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = { onIncrease(item.codigoProducto) },
                        onDecrease = { onDecrease(item.codigoProducto) },
                        onRemove = { onRemove(item.codigoProducto) }
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
    item: CarritoItemEntity,
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
                // val imagenUrl = item.imagenUrl ?: ""
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.nombreProducto,
                    // placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                    // error = painterResource(id = android.R.drawable.stat_notify_error),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(item.nombreProducto, fontWeight = FontWeight.Bold)
                    Text(formatPrice((item.precioUnitario * item.cantidad).toInt()))
                }
                IconButton(onClick = onRemove) {
                    // --- CORRECCIÓN AQUÍ ---
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
            
            // Row for quantity controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                 IconButton(onClick = onDecrease) {
                     Icon(Icons.Filled.Remove, contentDescription = "Disminuir")
                 }
                 Text(text = item.cantidad.toString())
                 IconButton(onClick = onIncrease) {
                     Icon(Icons.Filled.Add, contentDescription = "Aumentar")
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