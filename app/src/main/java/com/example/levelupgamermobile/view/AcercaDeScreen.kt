package com.example.levelupgamermobile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelupgamermobile.R

/**
 * pantalla estatica "acerca de" que muestra
 * informacion sobre el proyecto.
 *
 * @param onbackpress funcion lambda para manejar la navegacion
 * hacia atras (volver a [ProfileMenuScreen]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaDeScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("acerca de level-up gamer") },
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
        // usamos 'column' con 'verticalscroll' para asegurar
        // que el contenido quepa en pantallas pequenas.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. logo de la app
            Image(
                // usamos la referencia al logo que anadimos en 'res/drawable'
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo level-up gamer",
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 24.dp)
            )

            // 2. eslogan
            Text(
                "¡tu tienda para subir de nivel!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary, // usa el verde neon
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // 3. descripcion del proyecto
            Text(
                "level-up gamer es una aplicacion demo creada con jetpack compose y kotlin para la asignatura de programacion de aplicaciones moviles. este proyecto simula una tienda de e-commerce completa, desde la autenticacion de usuarios hasta un catalogo de productos y un carrito de compras funcional.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // 4. creditos
            Text(
                "desarrollado por:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Francisco Toloza & Bastian Corona",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}