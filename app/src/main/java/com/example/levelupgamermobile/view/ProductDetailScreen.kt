package com.example.levelupgamermobile.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupgamermobile.controller.ProductDetailUiState
import com.example.levelupgamermobile.controller.ProductDetailViewModel
import com.example.levelupgamermobile.model.Producto
import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import androidx.compose.material3.SnackbarHost // ¡NUEVO!
import androidx.compose.material3.SnackbarHostState // ¡NUEVO!
import androidx.compose.runtime.LaunchedEffect // ¡NUEVO!
import androidx.compose.runtime.remember // ¡NUEVO!
import androidx.compose.runtime.rememberCoroutineScope // ¡NUEVO!
import kotlinx.coroutines.flow.collectLatest // ¡NUEVO!
import kotlinx.coroutines.launch // ¡NUEVO!

/**
 * Pantalla "inteligente" (Smart Composable).
 */
@OptIn(ExperimentalMaterial3Api::class) // ¡NUEVO! Mover OptIn aquí
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // (1) ¡NUEVO! Configuración del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // (2) ¡NUEVO! "Escucha" los mensajes del ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    // (3) ¡CAMBIO!
    // Movemos el Scaffold aquí para que controle el Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // ¡NUEVO!
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ProductDetailContent(
            paddingValues = paddingValues, // ¡NUEVO! Pasa el padding
            uiState = uiState,
            onAddToCartClick = { viewModel.addToCart() },
            onAddReview = { calificacion, comentario ->
                viewModel.addReview(calificacion, comentario)
            }
        )
    }
}

/**
 * Composable "tonto" que solo dibuja la UI.
 */
@Composable
fun ProductDetailContent(
    paddingValues: PaddingValues, // ¡NUEVO! Recibe el padding
    uiState: ProductDetailUiState,
    onAddToCartClick: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    // (4) ¡CAMBIO! El Scaffold se fue
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), // ¡Aplica el padding!
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> { CircularProgressIndicator() }
            uiState.error != null -> {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.producto != null -> {
                ProductDetails(
                    producto = uiState.producto,
                    reviews = uiState.reviews,
                    averageRating = uiState.averageRating,
                    onAddToCartClick = onAddToCartClick,
                    onAddReview = onAddReview
                )
            }
        }
    }
}

/**
 * Dibuja los detalles del producto
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetails(
    producto: Producto,
    reviews: List<Resena>,
    averageRating: Float,
    onAddToCartClick: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- SLIDER DE IMÁGENES ---
        item {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { producto.imagenes.size }
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) { pageIndex ->
                Image(
                    painter = painterResource(id = producto.imagenes[pageIndex]),
                    contentDescription = "Imagen ${pageIndex + 1} de ${producto.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // --- INFORMACIÓN DEL PRODUCTO ---
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

                // Muestra la calificación promedio
                StarRatingDisplay(rating = averageRating, reviewCount = reviews.size)

                Text(
                    text = formatPrice(producto.precio),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
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

        // --- SECCIÓN DE RESEÑAS ---
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    "Reseñas y Calificaciones",
                    style = MaterialTheme.typography.titleLarge
                )
                // Formulario para añadir reseña
                AddReviewForm(onAddReview = onAddReview)

                Spacer(Modifier.height(16.dp))

                // Lista de reseñas
                if (reviews.isEmpty()) {
                    Text(
                        "Este producto aún no tiene reseñas. ¡Sé el primero!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    ReviewsList(reviews = reviews)
                }
            }
        }
    }
}

// --- FORMULARIO DE RESEÑA ---
@Composable
fun AddReviewForm(
    onAddReview: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Deja tu reseña:", style = MaterialTheme.typography.titleMedium)

        // Input de Estrellas
        StarRatingInput(
            rating = rating,
            onRatingChange = {
                rating = it
                showError = false
            }
        )

        OutlinedTextField(
            value = comentario,
            // V--- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---V
            onValueChange = { comentario = it }, // Era "onValueCodeChange"
            label = { Text("Escribe tu comentario...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        if (showError) {
            Text("Por favor, selecciona una calificación (1-5)", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (rating > 0) {
                    onAddReview(rating, comentario)
                    rating = 0
                    comentario = ""
                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("ENVIAR")
        }
    }
}

// --- LISTA DE RESEÑAS ---
@Composable
fun ReviewsList(reviews: List<Resena>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        reviews.forEach { review ->
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.fillMaxWidth().padding(12.dp)) {
                    Text(
                        review.userName, // Muestra solo el nombre de pila
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    StarRatingDisplay(rating = review.calificacion.toFloat())
                    Spacer(Modifier.height(4.dp))
                    Text(review.comentario, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

// --- HELPER: INPUT DE ESTRELLAS ---
@Composable
fun StarRatingInput(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Estrella $index",
                tint = LvlUpGreen,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingChange(index) }
            )
        }
    }
}

// --- HELPER: MOSTRAR ESTRELLAS (PROMEDIO) ---
@Composable
fun StarRatingDisplay(
    rating: Float,
    modifier: Modifier = Modifier,
    reviewCount: Int? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fullStars = floor(rating).toInt()
        val halfStar = ceil(rating) - floor(rating) > 0.4
        val emptyStars = 5 - fullStars - (if (halfStar) 1 else 0)

        repeat(fullStars) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = LvlUpGreen)
        }
        if (halfStar) {
            Icon(Icons.Filled.StarHalf, contentDescription = null, tint = LvlUpGreen)
        }
        repeat(emptyStars) {
            Icon(Icons.Filled.StarBorder, contentDescription = null, tint = LvlUpGreen)
        }

        if (reviewCount != null) {
            Text(
                " ($reviewCount)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

// --- HELPER DE PRECIO ---
private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}