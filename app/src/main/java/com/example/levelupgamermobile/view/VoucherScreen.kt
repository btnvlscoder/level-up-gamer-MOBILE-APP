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
import com.example.levelupgamermobile.utils.formatPrice // import de la funcion centralizada

/**
 * el "smart composable" para la pantalla de voucher (resumen de compra).
 *
 * esta funcion es responsable de:
 * 1. reutilizar el [CartViewModel] para acceder al estado del carrito
 * (los items y el total) *antes* de que se borre.
 * 2. observar el [CartUiState].
 * 3. manejar la logica de finalizacion de la compra cuando el boton
 * "finalizar" es presionado (guardar, limpiar y navegar).
 *
 * @param viewModel la instancia de [CartViewModel], obtenida por defecto.
 * @param onFinalizarClick funcion lambda para navegar de vuelta a la tienda
 * (a [BottomNavItem.Store]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(
    viewModel: CartViewModel = viewModel(),
    onFinalizarClick: () -> Unit
) {
    // 1. leemos el estado del viewmodel.
    //    este 'uistate' es el estado final del carrito antes
    //    de que se confirme la compra.
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("resumen de compra") })
        },
        bottomBar = {
            Button(
                onClick = {
                    // 1. guarda un registro permanente de la compra
                    //    en datastore a traves del viewmodel.
                    viewModel.savePurchase()
                    // 2. vacia el carrito (en [CartRepository]).
                    viewModel.clearCart()
                    // 3. navega de vuelta a la tienda.
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {
            // --- titulo ---
            item {
                Text(
                    "¡gracias por tu compra!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = LvlUpGreen
                )
                Spacer(Modifier.height(16.dp))
            }

            // --- lista de items ---
            items(uiState.items, key = { it.product.codigo }) { item ->
                VoucherItemRow(item = item)
            }

            // --- total ---
            item {
                Divider(Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("total pagado:", style = MaterialTheme.typography.titleLarge)
                    Text(
                        formatPrice(uiState.total),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * composable privado para dibujar una unica fila
 * de item en el resumen del voucher.
 *
 * @param item el [CartItem] a dibujar.
 */
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
                Text(item.product.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                formatPrice(item.product.precio * item.cantidad),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
