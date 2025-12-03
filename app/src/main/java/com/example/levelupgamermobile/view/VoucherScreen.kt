package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.CartViewModel
import com.example.levelupgamermobile.model.CartItem
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

// Data classes for JSON serialization
private data class VoucherProduct(
    val name: String,
    val quantity: Int,
    val price: Int
)

private data class VoucherData(
    val products: List<VoucherProduct>,
    val total: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(
    viewModel: CartViewModel = viewModel(),
    onFinalizarClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Create the JSON data
    val voucherDataAsJson = Gson().toJson(
        VoucherData(
            products = uiState.items.map {
                VoucherProduct(
                    name = it.producto.nombre,
                    quantity = it.cantidad,
                    price = it.producto.precio
                )
            },
            total = uiState.total
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Resumen de Compra") })
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.savePurchase()
                    viewModel.clearCart()
                    onFinalizarClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("FINALIZAR Y VOLVER A LA TIENDA")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar contenido
        ) {
            // (Título)
            item {
                Text(
                    "¡Gracias por tu compra!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = LvlUpGreen
                )
                Spacer(Modifier.height(16.dp))
            }

            // (Lista de items comprados)
            items(uiState.items, key = { it.producto.codigo }) { item ->
                VoucherItemRow(item = item)
            }

            // (Total)
            item {
                Divider(Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Pagado:", style = MaterialTheme.typography.titleLarge)
                    Text(
                        formatPrice(uiState.total),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(32.dp))
            }

            // (Código QR)
            item {
                // Pass the JSON string to the generator
                QrCodeGenerator(data = voucherDataAsJson)
            }
        }
    }
}

// --- (VoucherItemRow y formatPrice no cambian) ---

@Composable
private fun VoucherItemRow(item: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "Cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                formatPrice(item.producto.precio * item.cantidad),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}