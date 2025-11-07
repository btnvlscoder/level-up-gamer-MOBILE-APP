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
import com.example.levelupgamermobile.utils.formatPrice // import de la funcion centralizada

/**
 * el "smart composable" para la pantalla del carrito.
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [CartViewModel].
 * 2. observar el [uiState] del viewmodel.
 * 3. pasar el estado y las funciones (lambdas) de eventos
 * al composable "tonto" [CartScreenContent].
 *
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras.
 * @param onComprarClick funcion lambda para navegar al [VoucherScreen].
 */
@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(),
    onBackPress: () -> Unit,
    onComprarClick: () -> Unit
) {
    // observa el stateflow del viewmodel.
    // 'uistate' se actualizara automaticamente cada vez que
    // el estado en el viewmodel cambie (ej. al anadir un item).
    val uiState by viewModel.uiState.collectAsState()

    // delega la logica de la ui al composable "tonto".
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
 * el "dumb composable" (tonto) que solo dibuja la ui del carrito.
 *
 * no tiene logica de negocio. solo recibe el estado ([CartUiState])
 * y las funciones (lambdas) que debe ejecutar ante eventos.
 *
 * esto hace que esta funcion sea facil de previsualizar y probar.
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
                title = { Text("carrito de compras") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        },
        // la barra inferior solo se muestra si hay items en el carrito.
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // seccion del total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "total:",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            // usamos el 'formatprice' importado de 'utils'
                            formatPrice(uiState.total),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    // boton de compra
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
        // contenido principal (la lista)

        // si la lista esta vacia, muestra un mensaje centrado.
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("tu carrito esta vacio.")
            }
        } else {
            // si hay items, muestra la 'lazycolumn' (lista optimizada)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // itera sobre la lista de items y dibuja un 'cartitemrow' por cada uno
                items(uiState.items, key = { it.product.codigo }) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = { onIncrease(item.product.codigo) },
                        onDecrease = { onDecrease(item.product.codigo) },
                        onRemove = { onRemove(item.product.codigo) }
                    )
                }
            }
        }
    }
}

/**
 * composable para dibujar una unica fila (item) en el carrito.
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
            // seccion superior (imagen, info, boton de borrar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item.product.imagenes.first()),
                    contentDescription = item.product.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
                // columna de informacion (nombre, subtotal)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(item.product.nombre, fontWeight = FontWeight.Bold)
                    Text(formatPrice(item.product.precio * item.cantidad))
                }
                // boton de eliminar (tacho de basura)
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "eliminar"
                    )
                }
            }
            // seccion inferior (controles de cantidad)
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
                        contentDescription = "restar"
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
                        contentDescription = "sumar"
                    )
                }
            }
        }
    }
}
