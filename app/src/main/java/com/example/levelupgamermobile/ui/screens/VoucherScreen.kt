package com.example.levelupgamermobile.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.viewmodel.CartViewModel
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

import com.example.levelupgamermobile.data.local.entity.CarritoItemEntity
import com.example.levelupgamermobile.ui.components.QrCodeGenerator

// Data classes for JSON serialization
private data class VoucherProduct(
    val name: String,
    val quantity: Int,
    val price: Double
)

private data class VoucherData(
    val products: List<VoucherProduct>,
    val total: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(
    viewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onFinalizarClick: () -> Unit
) {
    val lastPurchase by viewModel.lastPurchase.collectAsStateWithLifecycle()
    val items = lastPurchase?.items ?: emptyList()
    val total = lastPurchase?.total ?: 0.0

    // Create the receipt-like string
    val sb = StringBuilder()
    sb.append("BOLETA DE COMPRA\n")
    sb.append("----------------\n")
    items.forEach { item ->
        sb.append("${item.nombreProducto} x ${item.cantidad}  ${formatPrice((item.precioUnitario * item.cantidad).toInt())}\n")
    }
    sb.append("----------------\n")
    sb.append("TOTAL: ${formatPrice(total.toInt())}")

    val voucherDataAsString = sb.toString()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Resumen de Compra") })
        },
        bottomBar = {
            Button(
                onClick = {
                    onFinalizarClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                // TODO: Color should be defined in Theme or MaterialTheme.colorScheme
                // colors = ButtonDefaults.buttonColors(containerColor = LvlUpGreen)
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
            items(items, key = { it.codigoProducto }) { item ->
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
                        formatPrice(total.toInt()),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(32.dp))
            }

            // (Código QR)
            item {
                // Pass the string to the generator
                QrCodeGenerator(data = voucherDataAsString)
            }
        }
    }
}

// --- (VoucherItemRow y formatPrice no cambian) ---

@Composable
private fun VoucherItemRow(item: CarritoItemEntity) {
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
                Text(item.nombreProducto, fontWeight = FontWeight.Bold)
                Text(
                    "Cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                formatPrice((item.precioUnitario * item.cantidad).toInt()),
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