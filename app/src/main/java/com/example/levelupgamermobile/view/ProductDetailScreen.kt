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
import com.example.levelupgamermobile.model.Product
import com.example.levelupgamermobile.model.Review
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import com.example.levelupgamermobile.utils.formatPrice
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor

/**
 * el "smart composable" para la pantalla de detalle de producto.
 *
 * esta funcion es responsable de:
 * 1. obtener la instancia del [ProductDetailViewModel].
 * 2. observar el [ProductDetailUiState].
 * 3. manejar eventos de un solo uso (como snackbars) con [LaunchedEffect].
 * 4. gestionar el [Scaffold] y [SnackbarHost] para esta pantalla.
 * 5. pasar el estado y los eventos al composable "tonto" [ProductDetailContent].
 *
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // 1. configuracion del snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 2. "escucha" los eventos de mensaje del viewmodel
    //    (ej. "resena ya existe" o "resena anadida")
    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    // 3. el scaffold principal que contiene la ui
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // 4. delega el dibujo del contenido
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
 * el "dumb composable" (tonto) que dibuja el cuerpo de la pantalla.
 *
 * esta funcion es responsable de decidir que mostrar:
 * - el indicador de carga (loading).
 * - el mensaje de error.
 * - la lista [ProductDetails] si todo esta correcto.
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
            .padding(paddingValues), // aplica el padding del scaffold
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
            uiState.product != null -> {
                // si el producto se cargo, dibuja los detalles
                ProductDetails(
                    producto = uiState.product,
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
 * el "dumb composable" (tonto) que dibuja el [LazyColumn]
 * con toda la informacion del producto y las resenas.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetails(
    producto: Product,
    reviews: List<Review>,
    averageRating: Float,
    onAddToCartClick: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- 1. slider de imagenes ---
        item {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { producto.imagenes.size }
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // mantiene relacion de aspecto cuadrada
            ) { pageIndex ->
                Image(
                    painter = painterResource(id = producto.imagenes[pageIndex]),
                    contentDescription = "imagen ${pageIndex + 1} de ${producto.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // --- 2. informacion del producto ---
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

                // helper para mostrar las estrellas (promedio)
                StarRatingDisplay(rating = averageRating, reviewCount = reviews.size)

                Text(
                    text = formatPrice(producto.precio), // usamos el util
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

        // --- 3. seccion de resenas ---
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    "resenas y calificaciones",
                    style = MaterialTheme.typography.titleLarge
                )
                // formulario para anadir resena
                AddReviewForm(onAddReview = onAddReview)

                Spacer(Modifier.height(16.dp))

                // lista de resenas existentes
                if (reviews.isEmpty()) {
                    Text(
                        "este producto aun no tiene resenas. ¡se el primero!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    ReviewsList(reviews = reviews)
                }
            }
        }
    }
}

/**
 * composable para el formulario de anadir resena.
 * gestiona su propio estado local (rating, comentario, error).
 */
@Composable
fun AddReviewForm(
    onAddReview: (Int, String) -> Unit
) {
    // estado local para el formulario
    var rating by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("deja tu resena:", style = MaterialTheme.typography.titleMedium)

        // input de estrellas
        StarRatingInput(
            rating = rating,
            onRatingChange = {
                rating = it
                showError = false // limpia el error al seleccionar
            }
        )

        OutlinedTextField(
            value = comentario,
            onValueChange = { comentario = it }, // <-- ¡CORREGIDO!
            label = { Text("escribe tu comentario...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        if (showError) {
            Text("por favor, selecciona una calificacion (1-5)", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                // validacion simple: debe tener calificacion
                if (rating > 0) {
                    onAddReview(rating, comentario)
                    // resetea el formulario
                    rating = 0
                    comentario = ""
                    showError = false
                } else {
                    showError = true // muestra el error
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("ENVIAR")
        }
    }
}

/**
 * dibuja la lista de resenas enviadas.
 */
@Composable
fun ReviewsList(reviews: List<Review>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        reviews.forEach { review ->
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.fillMaxWidth().padding(12.dp)) {
                    Text(
                        review.userName, // solo el nombre de pila
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

/**
 * un helper composable para dibujar las 5 estrellas
 * de *seleccion* (input).
 */
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
                contentDescription = "estrella $index",
                tint = LvlUpGreen, // usa el color verde neon
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingChange(index) }
            )
        }
    }
}

/**
 * un helper composable para *mostrar* (display)
 * una calificacion promedio, incluyendo medias estrellas.
 */
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

        // estrellas llenas
        repeat(fullStars) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = LvlUpGreen)
        }
        // media estrella
        if (halfStar) {
            Icon(Icons.Filled.StarHalf, contentDescription = null, tint = LvlUpGreen)
        }
        // estrellas vacias
        repeat(emptyStars) {
            Icon(Icons.Filled.StarBorder, contentDescription = null, tint = LvlUpGreen)
        }

        // numero opcional de resenas
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
