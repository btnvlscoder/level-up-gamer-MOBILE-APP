package com.example.levelupgamermobile.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.ProductListUiState
import com.example.levelupgamermobile.controller.ProductListViewModel
import com.example.levelupgamermobile.model.Producto
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.clickable

/**
 * Esta es la función "inteligente" (Smart Composable).
 * Su única responsabilidad es obtener el ViewModel y
 * observar el estado (uiState).
 */
// ... (imports) ...

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel(),
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ProductList(
        uiState = uiState,
        onProductClick = onProductClick,
        onCartClick = onCartClick// <-- PÁSALO A LA FUNCIÓN "TONTA"
    )
}

/**
 * Esta es la pantalla "tonta" (Dumb Composable).
 * No sabe nada de ViewModels. Solo recibe un estado y lo dibuja.
 * Es reutilizable y fácil de probar (testear).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(
    uiState: ProductListUiState,
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Level-Up Gamer") },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ir al carrito"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
            // LazyVerticalGrid es el equivalente a tu "contenedor-productos".
            // Es una grilla que "perezosamente" carga solo los items
            // que se ven en pantalla. Es muy eficiente.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "items" es un bucle que recorre la lista de productos
            items(uiState.productos, key = { it.codigo }) { producto ->
                ProductItem(
                    producto = producto,
                    onClick = { onProductClick(producto.codigo) }
                )
            }
        }
    }
}

/**
 * Este es el Composable para UN solo item de la grilla.
 * (Equivalente a tu div "producto" en HTML).
 */
@Composable
fun ProductItem(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        // modifier = Modifier.clickable { } // (Aquí irá la navegación al detalle)
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                // painterResource usa el ID (Int) de la imagen
                painter = painterResource(id = producto.imagenes.first()),
                contentDescription = producto.nombre,
                // ContentScale.Crop es como "object-fit: cover"
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    // aspectRatio(1f) fuerza a que sea un cuadrado perfecto
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = producto.marca,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Pone "..." si no cabe
                )
                Text(
                    text = formatPrice(producto.precio), // Función helper
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Función helper (ayudante) para formatear el precio.
 * Es el equivalente a tu "toLocaleString('es-CL')"
 */
private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    // En Chile, el formato CLP no usa decimales, así que los quitamos.
    format.maximumFractionDigits = 0
    return format.format(price)
}