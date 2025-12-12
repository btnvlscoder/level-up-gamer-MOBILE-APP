package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable // ¡NUEVO!
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.levelupgamermobile.ui.state.ProductListUiState
import com.example.levelupgamermobile.ui.viewmodel.ProductoViewModel
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.theme.LvlUpGreen
import com.example.levelupgamermobile.ui.theme.Orbitron // ¡NUEVO! Importa la fuente
import com.example.levelupgamermobile.utils.getImageResForProduct // Importa la utilidad de imágenes
import com.example.levelupgamermobile.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla "inteligente" (Smart Composable)
 */
@Composable
fun ProductListScreen(
    viewModel: ProductoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onProductClick: (String) -> Unit
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

    LaunchedEffect(uiState.filteredProducts.size) {
        println("ProductListScreen: Cargando ${uiState.filteredProducts.size} productos")
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LvlUpGreen
                    )
                ) {
                    Text(
                        text = data.visuals.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        ProductListContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onProductClick = onProductClick,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onCategoryChange = { viewModel.onCategoryChange(it) },
            onAddToCartClick = { viewModel.onAddToCartClick(it) }
        )
    }
}

/**
 * Contenido "tonto" (Dumb Composable)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    paddingValues: PaddingValues,
    uiState: ProductListUiState,
    onProductClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAddToCartClick: (ProductoEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // --- Cabecera: Título y Filtros ---
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // (1) ¡CAMBIO! Título estático con la fuente Orbitron
                Text(
                    text = "Level-Up Gamer",
                    style = MaterialTheme.typography.headlineSmall, // Título más grande
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Orbitron, // Usa la fuente "gamer"
                    fontWeight = FontWeight.Bold
                )

                // (2) Filtro de Búsqueda
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Buscar por nombre o marca...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LvlUpGreen,
                        focusedLabelColor = LvlUpGreen
                    )
                )

                // (3) Filtro de Categoría - Dropdown
                var isCategoryExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = uiState.categoryFilter,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isCategoryExpanded
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LvlUpGreen,
                            focusedLabelColor = LvlUpGreen
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false }
                    ) {
                        uiState.allCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    onCategoryChange(category)
                                    isCategoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- Lista de Productos ---
        items(uiState.filteredProducts, key = { it.codigo }) { producto ->
            ProductItem(
                producto = producto,
                onClick = { onProductClick(producto.codigo) },
                onAddToCartClick = { onAddToCartClick(producto) }
            )
        }
    }
}

/**
 * Un solo item del catálogo
 * (¡CORREGIDO para evitar clics conflictivos!)
 */
@Composable
fun ProductItem(
    producto: ProductoEntity,
    onClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        // La tarjeta ya NO es clickeable
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // (1) La IMAGEN es clickeable (para ir al detalle)
            // Usamos Coil AsyncImage para cargar desde URL o Recursos locales de forma segura
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getImageResForProduct(producto.codigo))
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder_image),
                error = painterResource(R.drawable.placeholder_image),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable(onClick = onClick) // <-- Clic aquí
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // (2) Los TEXTOS son clickeables (para ir al detalle)
                Column(modifier = Modifier.clickable(onClick = onClick)) { // <-- Clic aquí
                    Text(
                        text = producto.marca.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatPrice(producto.precio.toInt()), // Convertimos Double a Int para formatPrice
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // (3) El BOTÓN es separado (para agregar al carrito)
                Button(
                    onClick = onAddToCartClick, // <-- Clic separado
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("AGREGAR", style = MaterialTheme.typography.labelMedium)
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