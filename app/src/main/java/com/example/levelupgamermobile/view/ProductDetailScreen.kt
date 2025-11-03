package com.example.levelupgamermobile.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.levelupgamermobile.controller.ProductDetailUiState
import com.example.levelupgamermobile.controller.ProductDetailViewModel
import com.example.levelupgamermobile.model.Producto
import java.text.NumberFormat
import java.util.Locale

/**
 * Esta es la función "inteligente" (Smart Composable).
 * Es responsable de obtener el ViewModel y el estado.
 *
 * (1) ¡NUEVO!
 * Nota que ahora recibe un parámetro: "onBackPress".
 * Esto es para que podamos decirle al botón "Atrás" qué hacer.
 */
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    // Observamos el estado del ViewModel de detalle
    val uiState by viewModel.uiState.collectAsState()

    // Le pasamos el estado y la acción de "volver"
    // a nuestro Composable "tonto".
    ProductDetailContent(
        uiState = uiState,
        onBackPress = onBackPress,
        onAddToCartClick = { viewModel.addToCart() }
    )
}

/**
 * Este es el Composable "tonto" que dibuja la UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(
    uiState: ProductDetailUiState,
    onBackPress: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                // (2) ¡NUEVO!
                // Este es el botón de "atrás" en la barra superior.
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        // (3) ¡NUEVO!
        // Usamos un "Box" para centrar el indicador de carga
        // o el mensaje de error.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                // Caso 1: Estamos cargando
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                // Caso 2: Hubo un error
                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Caso 3: Tenemos el producto
                uiState.producto != null -> {
                    // Usamos LazyColumn para que todo sea deslizable
                    ProductDetails(
                        producto = uiState.producto,
                        onAddToCartClick = onAddToCartClick
                    )
                }
            }
        }
    }
}

/**
 * Este Composable dibuja los detalles del producto
 * cuando ya los tenemos.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetails(
    producto: Producto,
    onAddToCartClick: () -> Unit
) {
    // LazyColumn permite que el contenido sea "scrollable"
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // Quitamos el padding de la lista para que la imagen
        // ocupe todo el ancho.
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- (4) ¡NUEVO! SLIDER DE IMÁGENES ---
        item {
            // "rememberPagerState" guarda la página actual
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { producto.imagenes.size }
            )

            // "HorizontalPager" es el Composable que
            // permite deslizar entre páginas (imágenes).
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Lo hace cuadrado
            ) { pageIndex ->
                Image(
                        painter = painterResource(id = producto.imagenes[pageIndex]),
                    contentDescription = "Imagen ${pageIndex + 1} de ${producto.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // (Aquí podríamos agregar los "puntitos"
            // indicadores de la página, pero lo
            // mantendremos simple por ahora)
        }

        // --- info DEL PRODUCTO ---
        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = producto.marca.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatPrice(producto.precio), // Usamos el helper
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )

                //btn de Agregar
                Button(
                    onClick = onAddToCartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("AGREGAR AL CARRITO")
                }
            }
        }
    }
}

// --- (6) FUNCIÓN HELPER (AYUDANTE) ---

// NOTA: Esta función está duplicada de "ProductListScreen.kt".
// Más adelante, un buen paso de "refactorización" (limpieza)
// sería moverla a un nuevo archivo, por ejemplo:
// "utils/Formatters.kt", para no repetirla.

private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}