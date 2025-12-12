package com.example.levelupgamermobile.ui.screens

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
// import coil.compose.AsyncImage
import com.example.levelupgamermobile.ui.state.ProductDetailUiState
import com.example.levelupgamermobile.ui.viewmodel.ProductDetailViewModel
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import com.example.levelupgamermobile.data.local.entity.ResenaEntity
import com.example.levelupgamermobile.ui.components.StarRatingDisplay

/**
 * Pantalla "inteligente" (Smart Composable).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            paddingValues = paddingValues,
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
    paddingValues: PaddingValues,
    uiState: ProductDetailUiState,
    onAddToCartClick: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> { CircularProgressIndicator() }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.producto != null -> {
                ProductDetails(
                    producto = uiState.producto,
                    reviews = uiState.reviews,
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
    producto: ProductoEntity,
    reviews: List<ResenaEntity>,
    onAddToCartClick: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- IMAGEN DEL PRODUCTO (Local) ---
        item {
             Image(
                 painter = painterResource(id = producto.imageRes),
                 contentDescription = producto.nombre,
                 contentScale = ContentScale.Crop,
                 modifier = Modifier
                     .fillMaxWidth()
                     .aspectRatio(1f)
             )
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

                // Rating Display
                val averageRating = if (reviews.isNotEmpty()) {
                    reviews.map { it.calificacion }.average().toFloat()
                } else {
                    0f
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarRatingDisplay(rating = averageRating)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "(${reviews.size} opiniones)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

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
                
                if (reviews.isEmpty()) {
                    Text(
                        "No hay reseñas aún. ¡Sé el primero en opinar!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    reviews.forEach { review ->
                        ReviewItem(review)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Escribe tu opinión:", style = MaterialTheme.typography.titleMedium)
                AddReviewForm(onAddReview = onAddReview)
            }
        }
    }
}

@Composable
fun ReviewItem(review: ResenaEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(review.userName, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                StarRatingDisplay(
                    rating = review.calificacion.toFloat(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(review.comentario, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddReviewForm(onAddReview: (Int, String) -> Unit) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Calificación: ")
            (1..5).forEach { star ->
                Icon(
                    imageVector = if (star <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = null,
                    tint = LvlUpGreen,
                    modifier = Modifier
                        .clickable { rating = star }
                        .padding(4.dp)
                )
            }
        }
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (comment.isNotBlank()) {
                    onAddReview(rating, comment)
                    comment = ""
                    rating = 5
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("ENVIAR OPINIÓN")
        }
    }
}

// --- HELPER DE PRECIO ---
private fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}
