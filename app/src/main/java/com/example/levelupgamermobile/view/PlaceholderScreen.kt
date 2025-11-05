package com.example.levelupgamermobile.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * pantalla generica para secciones "en construccion".
 *
 * esta vista se reutiliza en el [NavHost] de [HomeScreen]
 * para las opciones del menu de perfil que aun no
 * tienen una pantalla dedicada (ej. [ProfileNavRoutes.TERMINOS]).
 *
 * @param title el texto a mostrar en la [TopAppBar].
 * @param onBackPress funcion lambda para manejar la navegacion hacia atras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    title: String,
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        // correccion: parametros 'imagevector' y 'contentdescription'
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // un 'box' simple que centra el mensaje
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("$title (en construccion)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}