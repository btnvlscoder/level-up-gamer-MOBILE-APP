package com.example.levelupgamermobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
// import coil.compose.AsyncImage
import com.example.levelupgamermobile.R
import com.example.levelupgamermobile.ui.AppViewModelProvider
import com.example.levelupgamermobile.ui.state.MisOpinionesUiState
import com.example.levelupgamermobile.ui.viewmodel.MisOpinionesViewModel
import com.example.levelupgamermobile.data.local.entity.ProductoEntity
import com.example.levelupgamermobile.model.Resena
import com.example.levelupgamermobile.ui.components.StarRatingDisplay

import androidx.compose.runtime.LaunchedEffect

/**
 * Pantalla "inteligente" que muestra las reseñas del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisOpinionesScreen(
    viewModel: MisOpinionesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Opiniones") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
             Box(
                 modifier = Modifier.fillMaxSize().padding(paddingValues),
                 contentAlignment = androidx.compose.ui.Alignment.Center
             ) {
                  androidx.compose.material3.CircularProgressIndicator()
             }
        } else {
            // (1) Mostramos la lista de opiniones
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.opiniones, key = { it.first.id }) { (reseña, producto) ->
                    OpinionItem(reseña = reseña, producto = producto)
                }
            }
        }
    }
}

/**
 * Un Composable para mostrar una sola opinión.
 */
@Composable
private fun OpinionItem(reseña: Resena, producto: ProductoEntity?) {
    Card(elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.fillMaxWidth()) {
            // (2) Mostramos la info del producto (si existe)
            if (producto != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = producto.imageRes),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            // (3) Mostramos la reseña del usuario
            Column(Modifier.padding(12.dp)) {
                StarRatingDisplay(rating = reseña.calificacion.toFloat())
                Spacer(Modifier.height(4.dp))
                if (reseña.comentario.isNotBlank()) {
                    Text(
                        text = reseña.comentario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

